package com.devexup.fabric.domain.repository;

import com.devexup.fabric.domain.model.NodePermission;
import java.util.List;
import java.util.UUID;

public interface NodePermissionRepository {
    List<NodePermission> findByRoleAndTenantId(String role, UUID tenantId);
    List<NodePermission> findByUserEmailAndTenantId(String email, UUID tenantId);
    NodePermission save(NodePermission permission);
}