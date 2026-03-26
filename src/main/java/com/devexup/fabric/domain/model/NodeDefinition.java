package com.devexup.fabric.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonRawValue;

@Getter
@Builder
public class NodeDefinition {
    private final UUID id;
    private final UUID tenantId;
    private final String name;
    @JsonRawValue
    private final String jsonSchema;
    @JsonRawValue
    private final String uiConfig;      
    private final String behaviorConfig; 
    private final int version;
}