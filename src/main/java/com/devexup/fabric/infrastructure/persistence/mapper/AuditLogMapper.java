package com.devexup.fabric.infrastructure.persistence.mapper;

import com.devexup.fabric.domain.model.AuditLog;
import com.devexup.fabric.infrastructure.persistence.jpa.AuditLogEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toDomain(AuditLogEntity entity) {
        if (entity == null) return null;
        return AuditLog.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .userEmail(entity.getUserEmail())
                .action(entity.getAction())
                .nodeName(entity.getNodeName())
                .recordId(entity.getRecordId())
                .timestamp(entity.getTimestamp())
                .details(entity.getDetails())
                .build();
    }

    public AuditLogEntity toEntity(AuditLog domain) {
        if (domain == null) return null;
        return AuditLogEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .userEmail(domain.getUserEmail())
                .action(domain.getAction())
                .nodeName(domain.getNodeName())
                .recordId(domain.getRecordId())
                .timestamp(domain.getTimestamp())
                .details(domain.getDetails())
                .build();
    }
}