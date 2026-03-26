package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.domain.service.PermissionAutomationService;
import com.devexup.fabric.infrastructure.config.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNodeDefinitionUseCase {

    private final NodeDefinitionRepository repository;
    private final PermissionAutomationService permissionAutomationService;

    @Transactional
    public NodeDefinition execute(NodeDefinition definition) {
        // 1. Guardar la definición en la DB
        NodeDefinition saved = repository.save(definition);

        // 2. AUTOMATIZACIÓN: El ADMIN del tenant actual recibe acceso total al nuevo nodo
        permissionAutomationService.grantFullAccess(
                TenantContext.getTenantId(),
                "ADMIN",
                saved.getName()
        );

        return saved;
    }
}