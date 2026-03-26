package com.devexup.fabric.infrastructure.persistence.mapper;

import com.devexup.fabric.domain.model.NodeEdge;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeEdgeEntity;
import org.springframework.stereotype.Component;

@Component
public class NodeEdgeMapper {

    public NodeEdge toDomain(NodeEdgeEntity entity) {
        if (entity == null) return null;
        return NodeEdge.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .sourceId(entity.getSourceId())
                .targetId(entity.getTargetId())
                .relationType(entity.getRelationType())
                .metadata(entity.getMetadata())
                .build();
    }

    public NodeEdgeEntity toEntity(NodeEdge domain) {
        if (domain == null) return null;
        return NodeEdgeEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .sourceId(domain.getSourceId())
                .targetId(domain.getTargetId())
                .relationType(domain.getRelationType())
                .metadata(domain.getMetadata())
                .build();
    }
}