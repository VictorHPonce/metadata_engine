package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.infrastructure.config.TenantContext;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeDefinitionEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeRecordEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeDefinitionRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeRecordRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.NodeRecordMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaDynamicRecordAdapter implements DynamicRecordRepository {

    private final SpringDataNodeRecordRepository jpaRepo;
    private final SpringDataNodeDefinitionRepository definitionRepo;
    private final JdbcClient jdbcClient;
    private final NodeRecordMapper recordMapper;

    private static final Set<String> ALLOWED_OPERATORS = Set.of("=", ">", "<", ">=", "<=", "!=", "LIKE");

    @Override
    @Transactional
    public DynamicRecord save(DynamicRecord record) {
        // 1. Buscamos la entidad de la definición PRIMERO
        NodeDefinitionEntity definitionEntity = definitionRepo.findById(record.getDefinitionId())
                .orElseThrow(() -> new RuntimeException("Definición no encontrada"));

        // 2. Pasamos todo al mapper. El mapper ahora devuelve la entidad COMPLETA.
        NodeRecordEntity entity = recordMapper.toEntity(record, definitionEntity);

        // 3. Forzamos los datos del contexto si es necesario (o lo dejas en el Mapper)
        entity.setTenantId(TenantContext.getTenantId());

        // 4. Guardamos y transformamos de vuelta a dominio
        return recordMapper.toDomain(jpaRepo.save(entity));
    }

    @Override
    public Optional<DynamicRecord> findById(UUID id) {
        return jpaRepo.findById(id).map(recordMapper::toDomain);
    }

    // Cumplimos con el contrato original (ahora redirigimos a Activos por defecto)
    @Override
    public List<DynamicRecord> findAllByNodeName(String nodeName) {
        return findAllActiveByNodeName(nodeName);
    }

    @Override
    public List<DynamicRecord> findAllActiveByNodeName(String nodeName) {
        UUID tenantId = TenantContext.getTenantId();
        return jpaRepo.findAllByTenantIdAndDefinitionNameAndDeletedAtIsNull(tenantId, nodeName).stream()
                .map(recordMapper::toDomain)
                .toList();
    }

    @Override
    public List<DynamicRecord> findAllDeletedByNodeName(String nodeName) {
        UUID tenantId = TenantContext.getTenantId();
        return jpaRepo.findAllByTenantIdAndDefinitionNameAndDeletedAtIsNotNull(tenantId, nodeName).stream()
                .map(recordMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DynamicRecord> findFirstByDefinitionNameOrderByCreatedAtDesc(String definitionName) {
        UUID tenantId = TenantContext.getTenantId();
        return jpaRepo
                .findFirstByTenantIdAndDefinitionNameAndDeletedAtIsNullOrderByCreatedAtDesc(tenantId, definitionName)
                .map(recordMapper::toDomain);
    }

    @Override
    public List<DynamicRecord> search(String nodeName, String field, String operator, Object value) {
        if (!ALLOWED_OPERATORS.contains(operator))
            throw new IllegalArgumentException("Operador inválido");

        UUID tenantId = TenantContext.getTenantId();
        String sql = String.format("""
                SELECT r.* FROM node_records r
                JOIN node_definitions d ON r.definition_id = d.id
                WHERE d.name = :nodeName
                AND r.tenant_id = :tenantId
                AND r.deleted_at IS NULL
                AND (r.data ->> :field) %s :value
                ORDER BY r.created_at DESC
                """, operator);

        return jdbcClient.sql(sql)
                .param("nodeName", nodeName)
                .param("tenantId", tenantId)
                .param("field", field)
                .param("value", value.toString())
                .query(NodeRecordEntity.class)
                .stream()
                .map(recordMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByLink(String nodeName, String link) {
        UUID tenantId = TenantContext.getTenantId();
        return jpaRepo.existsByTenantIdAndDefinitionNameAndLink(tenantId, nodeName, link);
    }

    @Override
    public void softDelete(UUID id) {
        jpaRepo.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(java.time.OffsetDateTime.now());
            jpaRepo.save(entity);
        });
    }

    @Override
    public void restore(UUID id) {
        jpaRepo.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(null);
            jpaRepo.save(entity);
        });
    }

    @Override
    public Long countByNodeNameAndTenantIdAndDeletedAtIsNotNull(String nodeName, UUID tenantId) {
        // Simplemente delegamos al SpringData que ya tiene el método
        return jpaRepo.countByNodeNameAndTenantIdAndDeletedAtIsNotNull(nodeName, tenantId);
    }

    @Override
    @Transactional
    public DynamicRecord update(DynamicRecord record) {
        // 1. Buscamos la entidad de la definición (Igual que en el save)
        NodeDefinitionEntity definitionEntity = definitionRepo.findById(record.getDefinitionId())
                .orElseThrow(() -> new RuntimeException("Definición no encontrada para actualizar"));

        // 2. Usamos NodeRecordEntity (que es la clase que tienes importada arriba)
        // Y llamamos al mapper con los dos argumentos que requiere
        NodeRecordEntity entity = recordMapper.toEntity(record, definitionEntity);

        // 3. Importante: Aseguramos que el tenant no cambie y que el ID sea el correcto
        entity.setTenantId(TenantContext.getTenantId());
        
        // 4. JPA save() hará el update automáticamente porque la entidad ya lleva su UUID
        NodeRecordEntity savedEntity = jpaRepo.save(entity);
        
        return recordMapper.toDomain(savedEntity);
    }

}