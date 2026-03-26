package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.model.NodeEdge;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.domain.repository.NodeEdgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRelatedRecordsUseCase {

    private final NodeEdgeRepository edgeRepository;
    private final DynamicRecordRepository recordRepository;

    /**
     * Busca todos los registros que apuntan a este ID (Consulta Inversa)
     * Ej: Si le paso el ID de Messi, me dará todos sus Pedidos.
     */
    public List<DynamicRecord> executeInverse(UUID targetId) {
        // 1. Buscamos los Edges donde el registro sea el TARGET
        List<NodeEdge> edges = edgeRepository.findByTargetId(targetId);

        // 2. Por cada Edge, obtenemos el registro ORIGEN
        return edges.stream()
                .map(edge -> recordRepository.findById(edge.getSourceId()).orElse(null))
                .filter(record -> record != null)
                .collect(Collectors.toList());
    }
}