package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.domain.service.PermissionAutomationService; // <--- Inyectar
import com.devexup.fabric.infrastructure.config.TenantContext; // <--- Para el ID del tenant
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterNodeUseCase {

    private final NodeDefinitionRepository repository;
    private final PermissionAutomationService permissionAutomationService;

    @Transactional
    // 1. Agregamos behaviorConfig a los parámetros
    public NodeDefinition execute(String name, String schema, String uiConfig, String behaviorConfig) { 
        
        // 2. Ahora sí podemos usar la variable en el builder
        NodeDefinition definition = NodeDefinition.builder()
                .id(UUID.randomUUID())
                .tenantId(TenantContext.getTenantId())
                .name(name)
                .jsonSchema(schema)
                .uiConfig(uiConfig)
                .behaviorConfig(behaviorConfig)
                .version(1)
                .build();

        NodeDefinition saved = repository.save(definition);

        permissionAutomationService.grantFullAccess(
                saved.getTenantId(),
                "ADMIN",
                saved.getName()
        );

        return saved;
    }
}