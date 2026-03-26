package com.devexup.fabric.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "node_definitions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NodeDefinitionEntity {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false) // NUEVO
    private UUID tenantId;

    @Column(nullable = false) // Quitamos unique=true porque puede repetirse en distintos tenants
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String schema;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ui_config", columnDefinition = "jsonb")
    private String uiConfig;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "behavior_config", columnDefinition = "jsonb")
    private String behaviorConfig;

    private int version;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID();
        this.createdAt = OffsetDateTime.now();
    }
}