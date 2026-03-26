package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeDefinitionEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeDefinitionRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.NodeDefinitionMapper; // <-- Nuevo
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaNodeDefinitionAdapter implements NodeDefinitionRepository {

    private final SpringDataNodeDefinitionRepository jpaRepo;
    private final NodeDefinitionMapper definitionMapper;

    @Override
    public NodeDefinition save(NodeDefinition node) {
        NodeDefinitionEntity entity = definitionMapper.toEntity(node);
        
        // ASIGNACIÓN AUTOMÁTICA DEL TENANT AL GUARDAR
        // Extraemos el tenant del hilo actual (puesto por el Interceptor o el Scraper)
        entity.setTenantId(com.devexup.fabric.infrastructure.config.TenantContext.getTenantId());
        
        NodeDefinitionEntity saved = jpaRepo.save(entity);
        return definitionMapper.toDomain(saved);
    }

    @Override
    public Optional<NodeDefinition> findByName(String name) {
        // FILTRO AUTOMÁTICO POR TENANT AL BUSCAR
        UUID tenantId = com.devexup.fabric.infrastructure.config.TenantContext.getTenantId();
        return jpaRepo.findByTenantIdAndName(tenantId, name)
                .map(definitionMapper::toDomain);
    }

    @Override
    public List<NodeDefinition> findAll() {
        // Un administrador solo ve sus propios nodos
        UUID tenantId = com.devexup.fabric.infrastructure.config.TenantContext.getTenantId();
        return jpaRepo.findAllByTenantId(tenantId).stream()
                .map(definitionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<NodeDefinition> findById(UUID id) {
        // Al buscar por ID, también podríamos validar que pertenece al tenant
        return jpaRepo.findById(id)
                .filter(entity -> entity.getTenantId().equals(com.devexup.fabric.infrastructure.config.TenantContext.getTenantId()))
                .map(definitionMapper::toDomain);
    }
}