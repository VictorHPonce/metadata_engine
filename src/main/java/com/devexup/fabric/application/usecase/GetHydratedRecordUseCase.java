package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.model.NodeEdge;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.domain.repository.NodeEdgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHydratedRecordUseCase {

    private final DynamicRecordRepository recordRepository;
    private final NodeEdgeRepository edgeRepository;

    public Map<String, Object> execute(UUID recordId) {
        // 1. Buscar el registro base (ej. el Pedido)
        DynamicRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        // 2. Buscar sus conexiones (Edges)
        List<NodeEdge> edges = edgeRepository.findBySourceId(recordId);

        // 3. "Hidratar": Por cada conexión, buscar los datos del destino
        List<Map<String, Object>> connections = edges.stream().map(edge -> {
            Map<String, Object> connData = new HashMap<>();
            connData.put("relation", edge.getRelationType());
            
            // Buscamos el registro destino (ej. el Cliente)
            recordRepository.findById(edge.getTargetId()).ifPresent(target -> {
                connData.put("target_id", target.getId());
                connData.put("target_data", target.getData());
            });
            
            return connData;
        }).collect(Collectors.toList());

        // 4. Montar la respuesta final
        Map<String, Object> response = new HashMap<>();
        response.put("record", record);
        response.put("connections", connections);

        return response;
    }
}