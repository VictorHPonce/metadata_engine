package com.devexup.fabric.web.controller;

import com.devexup.fabric.application.usecase.*;
import com.devexup.fabric.domain.model.PermissionType;
import com.devexup.fabric.infrastructure.security.PermissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nodes")
@RequiredArgsConstructor
public class DynamicRecordController {

    private final SubmitRecordUseCase submitRecordUseCase;
    private final GetRecordsUseCase getRecordsUseCase;
    private final DeleteRecordUseCase deleteRecordUseCase;
    private final GetHydratedRecordUseCase getHydratedRecordUseCase;
    private final GetRelatedRecordsUseCase getRelatedRecordsUseCase;
    private final PermissionService permissionService;
    private final ObjectMapper mapper;
    private final UpdateRecordUseCase updateRecordUseCase;

    @PostMapping("/{nodeName}/records")
    public ResponseEntity<?> createRecord(@PathVariable String nodeName, @RequestBody Map<String, Object> data) {
        if (!permissionService.hasAccess(nodeName, PermissionType.WRITE)) {
            return forbiddenResponse(nodeName, PermissionType.WRITE);
        }
        try {
            String jsonData = mapper.writeValueAsString(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(submitRecordUseCase.execute(nodeName, jsonData));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{nodeName}/records")
    public ResponseEntity<?> getRecords(
            @PathVariable String nodeName,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value) {

        if (!permissionService.hasAccess(nodeName, PermissionType.READ)) {
            return forbiddenResponse(nodeName, PermissionType.READ);
        }

        if (field != null && operator != null && value != null) {
            return ResponseEntity.ok(getRecordsUseCase.executeWithFilter(nodeName, field, operator, value));
        }
        return ResponseEntity.ok(getRecordsUseCase.execute(nodeName));
    }

    @DeleteMapping("/{nodeName}/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable String nodeName, @PathVariable UUID id) {
        // Verificamos permisos sobre el nodo antes de borrar
        if (!permissionService.hasAccess(nodeName, PermissionType.WRITE)) {
            return forbiddenResponse(nodeName, PermissionType.WRITE);
        }

        deleteRecordUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/records/{id}/hydrated")
    public ResponseEntity<?> getHydrated(@PathVariable UUID id) {
        return ResponseEntity.ok(getHydratedRecordUseCase.execute(id));
    }

    @GetMapping("/records/{id}/incoming")
    public ResponseEntity<?> getIncoming(@PathVariable UUID id) {
        return ResponseEntity.ok(getRelatedRecordsUseCase.executeInverse(id));
    }

    @PatchMapping("/{nodeName}/records/{id}")
    public ResponseEntity<?> patchRecord(@PathVariable String nodeName, @PathVariable UUID id,
            @RequestBody Map<String, Object> updates) {
        if (!permissionService.hasAccess(nodeName, PermissionType.WRITE)) {
            return forbiddenResponse(nodeName, PermissionType.WRITE);
        }
        // Tu UseCase debería soportar una actualización parcial
        return ResponseEntity.ok(updateRecordUseCase.executePartial(id, updates));
    }

    private ResponseEntity<String> forbiddenResponse(String node, PermissionType type) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(String.format("Acceso denegado: Se requiere permiso %s para el nodo '%s'", type, node));
    }
}