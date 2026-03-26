package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.NodeEdge;
import com.devexup.fabric.domain.repository.NodeEdgeRepository;
import com.devexup.fabric.infrastructure.config.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectNodesUseCase {

    private final NodeEdgeRepository edgeRepository;

    public NodeEdge execute(UUID sourceId, UUID targetId, String relationType) {
        // Obtenemos el dueño actual del token
        UUID currentTenantId = TenantContext.getTenantId();

        NodeEdge edge = NodeEdge.builder()
                .id(UUID.randomUUID())
                .tenantId(currentTenantId)
                .sourceId(sourceId)
                .targetId(targetId)
                .relationType(relationType)
                .build();

        return edgeRepository.save(edge);
    }
}