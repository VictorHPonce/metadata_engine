package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.domain.repository.NodeAggregationRepository;
import com.devexup.fabric.infrastructure.security.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GetNodeAggregationUseCase {

    private final NodeAggregationRepository aggregationRepository;
    private final PermissionService permissionService;

    public BigDecimal execute(String nodeName, String fieldName, String operation) {
        // 1. Validar permiso de lectura sobre el nodo
        if (!permissionService.hasAccess(nodeName, PermissionType.READ)) {
            throw new RuntimeException("No tienes permiso para ver los datos de " + nodeName);
        }

        // 2. Ejecutar agregación
        BigDecimal result = aggregationRepository.aggregate(nodeName, fieldName, operation);
        
        return result != null ? result : BigDecimal.ZERO;
    }
}