package com.devexup.fabric.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "node_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NodeRecordEntity {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "node_name", nullable = false) // VITAL para Auditoría y Consultas rápidas
    private String nodeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id", nullable = false)
    private NodeDefinitionEntity definition;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String data;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // Cambiamos a OffsetDateTime para mantener consistencia con createdAt/updatedAt
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    // Helper para obtener el ID de la definición sin cargar todo el objeto
    public UUID getDefinitionId() {
        return definition != null ? definition.getId() : null;
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID();
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}