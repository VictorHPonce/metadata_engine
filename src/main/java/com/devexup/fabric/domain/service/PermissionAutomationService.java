package com.devexup.fabric.domain.service;

import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.domain.repository.NodePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionAutomationService {

    private final NodePermissionRepository permissionRepository;

    /**
     * Otorga permisos totales (READ, WRITE, DELETE) a un rol sobre un nodo.
     */
    public void grantFullAccess(UUID tenantId, String role, String nodeName) {
        grantPermission(tenantId, role, nodeName, PermissionType.READ);
        grantPermission(tenantId, role, nodeName, PermissionType.WRITE);
        grantPermission(tenantId, role, nodeName, PermissionType.DELETE);
    }

    private void grantPermission(UUID tenantId, String role, String nodeName, PermissionType type) {
        NodePermission perm = NodePermission.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .role(role)
                .nodeName(nodeName)
                .permission(type)
                .build();
        permissionRepository.save(perm);
    }
}