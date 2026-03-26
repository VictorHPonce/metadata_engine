package com.devexup.fabric.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NodeDefinitionRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El esquema es obligatorio")
    @JsonProperty("schema") // Coincide con tu JSON
    private Object schema;

    @JsonProperty("uiConfig") // CAMBIO: Quitamos el guion bajo para que coincida con tu Postman
    private Object uiConfig;

    @JsonProperty("behaviorConfig") // NUEVO: Para las fórmulas (Calculated Fields)
    private Object behaviorConfig;
}