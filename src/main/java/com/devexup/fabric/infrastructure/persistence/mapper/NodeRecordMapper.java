package com.devexup.fabric.infrastructure.persistence.mapper;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeDefinitionEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeRecordEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeRecordMapper {
    // Si necesitas la entidad de definición para el mapeo inverso
    public DynamicRecord toDomain(NodeRecordEntity entity) {
        if (entity == null) return null;
        return DynamicRecord.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .nodeName(entity.getNodeName())
                .definitionId(entity.getDefinitionId())
                .data(entity.getData())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public NodeRecordEntity toEntity(DynamicRecord domain, NodeDefinitionEntity definition) {
        if (domain == null) return null;
        return NodeRecordEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .nodeName(domain.getNodeName())
                .definition(definition) // Necesita la entidad completa por ser @ManyToOne
                .data(domain.getData())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .deletedAt(domain.getDeletedAt())
                .build();
    }
}