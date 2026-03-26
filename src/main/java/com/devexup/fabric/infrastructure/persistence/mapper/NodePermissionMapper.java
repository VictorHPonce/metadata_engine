package com.devexup.fabric.infrastructure.persistence.mapper;

import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.infrastructure.persistence.jpa.NodePermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class NodePermissionMapper {

    public NodePermission toDomain(NodePermissionEntity entity) {
        if (entity == null) return null;
        return NodePermission.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .role(entity.getRole())
                .userEmail(entity.getUserEmail())
                .nodeName(entity.getNodeName())
                .permission(entity.getPermission())
                .build();
    }

    public NodePermissionEntity toEntity(NodePermission domain) {
        if (domain == null) return null;
        return NodePermissionEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .role(domain.getRole())
                .userEmail(domain.getUserEmail())
                .nodeName(domain.getNodeName())
                .permission(domain.getPermission())
                .build();
    }
}