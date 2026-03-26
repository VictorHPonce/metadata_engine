package com.devexup.fabric.web.controller;

import com.devexup.fabric.application.dto.NodeDefinitionRequest;
import com.devexup.fabric.application.usecase.*;
import com.devexup.fabric.domain.model.NodeDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/definitions")
@RequiredArgsConstructor
public class NodeDefinitionController {

    private final RegisterNodeUseCase registerNodeUseCase;
    private final GetAllDefinitionsUseCase getAllDefinitionsUseCase;
    private final GetDefinitionUseCase getDefinitionUseCase;
    private final UpdateDefinitionUseCase updateDefinitionUseCase;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<NodeDefinition>> getAll() {
        return ResponseEntity.ok(getAllDefinitionsUseCase.execute());
    }

    @GetMapping("/{name}")
    public ResponseEntity<NodeDefinition> getByName(@PathVariable String name) {
        return ResponseEntity.ok(getDefinitionUseCase.execute(name));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NodeDefinitionRequest request) {
        try {
            String schemaStr = objectMapper.writeValueAsString(request.getSchema());

            // Procesamos uiConfig
            String uiConfigStr = request.getUiConfig() != null
                    ? objectMapper.writeValueAsString(request.getUiConfig())
                    : null;

            // Procesamos behaviorConfig (Fórmulas)
            String behaviorConfigStr = request.getBehaviorConfig() != null
                    ? objectMapper.writeValueAsString(request.getBehaviorConfig())
                    : null;

            NodeDefinition result = registerNodeUseCase.execute(
                    request.getName(),
                    schemaStr,
                    uiConfigStr,
                    behaviorConfigStr // Asegúrate de que tu UseCase acepte este 4º parámetro
            );

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @Valid @RequestBody NodeDefinitionRequest request) {
        try {
            String schemaStr = objectMapper.writeValueAsString(request.getSchema());

            String uiConfigStr = request.getUiConfig() != null
                    ? objectMapper.writeValueAsString(request.getUiConfig())
                    : null;

            // NUEVO: No olvidar capturar el behaviorConfig del request
            String behaviorConfigStr = request.getBehaviorConfig() != null
                    ? objectMapper.writeValueAsString(request.getBehaviorConfig())
                    : null;

            // Construimos el modelo con TODOS los campos
            NodeDefinition updateModel = NodeDefinition.builder()
                    .name(request.getName()) // Mantener el nombre si se envía
                    .jsonSchema(schemaStr)
                    .uiConfig(uiConfigStr)
                    .behaviorConfig(behaviorConfigStr) // <--- ESTO FALTABA
                    .build();

            return ResponseEntity.ok(updateDefinitionUseCase.execute(id, updateModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}