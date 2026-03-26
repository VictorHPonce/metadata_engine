package com.devexup.fabric.application.service;

import com.devexup.fabric.domain.model.AuditLog;
import com.devexup.fabric.domain.repository.AuditLogRepository;
import com.devexup.fabric.infrastructure.config.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service; 

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String nodeName, UUID recordId, String details) {
        // Obtenemos el email del usuario del contexto de seguridad de Spring
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        AuditLog log = AuditLog.builder()
                .id(UUID.randomUUID())
                .tenantId(TenantContext.getTenantId())
                .userEmail(userEmail != null ? userEmail : "SYSTEM")
                .action(action)
                .nodeName(nodeName)
                .recordId(recordId)
                .timestamp(Instant.now())
                .details(details)
                .build();

        auditLogRepository.save(log);
    }
}