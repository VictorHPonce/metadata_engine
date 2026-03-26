package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataNodePermissionRepository extends JpaRepository<NodePermissionEntity, UUID> {
    List<NodePermissionEntity> findByRoleAndTenantId(String role, UUID tenantId);
    List<NodePermissionEntity> findByUserEmailAndTenantId(String userEmail, UUID tenantId);
}