package com.devexup.fabric.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class AuditLog {
    private final UUID id;
    private final UUID tenantId;
    private final String userEmail;
    private final String action; // CREATE, UPDATE, DELETE, RESTORE
    private final String nodeName;
    private final UUID recordId;
    private final Instant timestamp;
    private final String details; // Opcional: un resumen de qué cambió
}