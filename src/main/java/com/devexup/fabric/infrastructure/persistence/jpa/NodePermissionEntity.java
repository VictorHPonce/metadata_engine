package com.devexup.fabric.infrastructure.persistence.jpa;

import com.devexup.fabric.domain.model.PermissionType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "node_permissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NodePermissionEntity {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String role; // ej: ADMIN, REPARTIDOR, CAJERO

    @Column(name = "user_email") 
    private String userEmail;

    @Column(name = "node_name", nullable = false)
    private String nodeName; // ej: Pedidos, Clientes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionType permission; // READ, WRITE, DELETE
}