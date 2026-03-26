package com.devexup.fabric.infrastructure.persistence.mapper;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeDefinitionEntity;
import org.springframework.stereotype.Component;

@Component
public class NodeDefinitionMapper {
    public NodeDefinition toDomain(NodeDefinitionEntity entity) {
        if (entity == null) return null;
        return NodeDefinition.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .jsonSchema(entity.getSchema())
                .uiConfig(entity.getUiConfig())
                .behaviorConfig(entity.getBehaviorConfig())
                .version(entity.getVersion())
                .build();
    }

    public NodeDefinitionEntity toEntity(NodeDefinition domain) {
        if (domain == null) return null;
        return NodeDefinitionEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .schema(domain.getJsonSchema())
                .uiConfig(domain.getUiConfig())
                .behaviorConfig(domain.getBehaviorConfig())
                .version(domain.getVersion())
                .build();
    }
}