package com.devexup.fabric.domain.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NodeEdge {
    private final UUID id;
    private final UUID tenantId;
    private final UUID sourceId; // ID del Record origen
    private final UUID targetId; // ID del Record destino
    private final String relationType;
    @JsonRawValue
    private final String metadata;
}
