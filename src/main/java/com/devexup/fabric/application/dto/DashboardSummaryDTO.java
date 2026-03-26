package com.devexup.fabric.application.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardSummaryDTO {
    private long totalDefinitions;
    private long totalRecords;
    private Map<String, Long> recordsPerNode;
    private String lastActivity; // Fecha formateada
}