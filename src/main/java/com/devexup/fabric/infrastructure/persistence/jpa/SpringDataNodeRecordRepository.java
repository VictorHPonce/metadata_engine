package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataNodeRecordRepository extends JpaRepository<NodeRecordEntity, UUID> {
    
    // --- CONSULTAS PARA REGISTROS ACTIVOS ---
    
    // Solo registros que NO están borrados (deletedAt es null)
    List<NodeRecordEntity> findAllByTenantIdAndDefinitionNameAndDeletedAtIsNull(UUID tenantId, String name);

    // El último registro activo
    Optional<NodeRecordEntity> findFirstByTenantIdAndDefinitionNameAndDeletedAtIsNullOrderByCreatedAtDesc(UUID tenantId, String name);

    // --- CONSULTAS PARA LA PAPELERA (TRASH) ---

    // Solo registros que SÍ están borrados
    List<NodeRecordEntity> findAllByTenantIdAndDefinitionNameAndDeletedAtIsNotNull(UUID tenantId, String name);

    // --- UTILIDADES ---

    @Query(value = "SELECT COUNT(*) > 0 FROM node_records r " +
                   "JOIN node_definitions d ON r.definition_id = d.id " +
                   "WHERE r.tenant_id = :tenantId AND d.name = :name " +
                   "AND r.data ->> 'link' = :link AND r.deleted_at IS NULL", nativeQuery = true)
    boolean existsByTenantIdAndDefinitionNameAndLink(UUID tenantId, String name, String link);

    // Conteo de registros activos para el Dashboard
    long countByNodeNameAndDeletedAtIsNull(String nodeName);

    // Spring Data generará el COUNT(*) basándose en este nombre de método
    long countByNodeNameAndTenantIdAndDeletedAtIsNotNull(String nodeName, UUID tenantId);

    // También asegúrate de tener este para el execute() del UseCase
    List<NodeRecordEntity> findAllByNodeNameAndTenantIdAndDeletedAtIsNotNull(String nodeName, UUID tenantId);
}