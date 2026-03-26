package com.devexup.fabric.application.usecase;

import java.util.UUID;
import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.infrastructure.security.PermissionService;
import com.devexup.fabric.infrastructure.config.TenantContext;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateDefinitionUseCase {

    private final NodeDefinitionRepository repository;
    private final PermissionService permissionService;

    @Transactional
    public NodeDefinition execute(UUID id, NodeDefinition updatedData) {
        // 1. Buscar la definición actual
        NodeDefinition existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Definición no encontrada con ID: " + id));

        // 2. SEGURIDAD: Verificar que la definición pertenece al Tenant actual
        // (Aunque el RLS de Postgres lo bloquee, es buena práctica validarlo en la capa de aplicación)
        if (!existing.getTenantId().equals(TenantContext.getTenantId())) {
            throw new RuntimeException("No tiene permisos para modificar recursos de otro Tenant");
        }

        // 3. ACL: Verificar si el usuario tiene permiso de ESCRITURA (WRITE) sobre este nodo
        if (!permissionService.hasAccess(existing.getName(), PermissionType.WRITE)) {
            throw new RuntimeException("Acceso denegado: Se requiere permiso WRITE para el nodo " + existing.getName());
        }

        // 4. Mapeo y Evolución de Versión
        NodeDefinition toSave = NodeDefinition.builder()
                .id(existing.getId())
                .tenantId(existing.getTenantId()) // Mantenemos el dueño original
                .name(existing.getName())         // El nombre del nodo suele ser inmutable
                .jsonSchema(updatedData.getJsonSchema() != null ? updatedData.getJsonSchema() : existing.getJsonSchema())
                .uiConfig(updatedData.getUiConfig() != null ? updatedData.getUiConfig() : existing.getUiConfig())
                .behaviorConfig(existing.getBehaviorConfig())
                .version(existing.getVersion() + 1) // Incremento de versión automático
                .build();

        return repository.save(toSave);
    }
}