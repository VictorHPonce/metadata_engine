package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.application.dto.DashboardSummaryDTO;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeRecordRepository; // Necesitarás este repo
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetDashboardSummaryUseCase {

    private final NodeDefinitionRepository definitionRepository;
    private final SpringDataNodeRecordRepository recordRepository; 

    public DashboardSummaryDTO execute() {
        var definitions = definitionRepository.findAll();
        
        Map<String, Long> stats = new HashMap<>();
        long totalRecords = 0;

        for (var def : definitions) {
            // Usamos el método que ignora los borrados
            long count = recordRepository.countByNodeNameAndDeletedAtIsNull(def.getName());
            stats.put(def.getName(), count);
            totalRecords += count;
        }

        return DashboardSummaryDTO.builder()
                .totalDefinitions(definitions.size())
                .totalRecords(totalRecords)
                .recordsPerNode(stats)
                .lastActivity("Hoy")
                .build();
    }
}