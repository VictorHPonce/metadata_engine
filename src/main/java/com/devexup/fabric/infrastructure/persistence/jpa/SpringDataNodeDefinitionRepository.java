package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataNodeDefinitionRepository extends JpaRepository<NodeDefinitionEntity, UUID> {
    // Optional<NodeDefinitionEntity> findByNameIgnoreCase(String name);
    Optional<NodeDefinitionEntity> findByTenantIdAndName(UUID tenantId, String name);
    List<NodeDefinitionEntity> findAllByTenantId(UUID tenantId);
}