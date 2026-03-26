package com.devexup.fabric.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.domain.repository.NodePermissionRepository;
import com.devexup.fabric.infrastructure.config.TenantContext;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/permissions")
@RequiredArgsConstructor
public class UserPermissionController {

    private final NodePermissionRepository permissionRepository;

    @PostMapping("/assign")
    public ResponseEntity<?> assign(@RequestBody PermissionRequest req) {
        NodePermission perm = NodePermission.builder()
                .id(UUID.randomUUID())
                .tenantId(TenantContext.getTenantId())
                .userEmail(req.getEmail()) // Ahora ya no dará error
                .nodeName(req.getNodeName())
                .permission(req.getType()) // Coincide con el nombre en tu modelo
                .build();

        permissionRepository.save(perm);
        return ResponseEntity.ok("Permiso asignado correctamente");
    }
}

@Data
class PermissionRequest {
    private String email;
    private String nodeName;
    private PermissionType type; // READ o WRITE
}