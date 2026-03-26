package com.devexup.fabric.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLogEntity {
    @Id
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "user_email", nullable = false, updatable = false)
    private String userEmail;

    @Column(nullable = false, updatable = false)
    private String action; // CREATE, UPDATE, DELETE, RESTORE

    @Column(name = "node_name", nullable = false, updatable = false)
    private String nodeName;

    @Column(name = "record_id", nullable = false, updatable = false)
    private UUID recordId;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @Column(columnDefinition = "TEXT", updatable = false)
    private String details;
}
