package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.AuditLog;
import com.devexup.fabric.domain.repository.AuditLogRepository;
import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.infrastructure.security.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;
    private final PermissionService permissionService;

    public List<AuditLog> execute(String nodeName) {
        // 1. Verificar que el usuario tiene permiso de lectura en el nodo
        if (!permissionService.hasAccess(nodeName, PermissionType.READ)) {
            throw new RuntimeException("No tiene permisos para ver la auditoría de " + nodeName);
        }

        // 2. Recuperar logs (el adaptador ya filtra por Tenant vía RLS o consulta)
        return auditLogRepository.findByNodeName(nodeName);
    }

    public List<AuditLog> executeByRecord(UUID recordId) {
        // Útil para ver el historial de un solo registro específico
        return auditLogRepository.findByRecordId(recordId);
    }
}