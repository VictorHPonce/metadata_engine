package com.devexup.fabric.web.controller;

import com.devexup.fabric.application.usecase.GetAuditLogsUseCase;
import com.devexup.fabric.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final GetAuditLogsUseCase getAuditLogsUseCase;

    @GetMapping("/nodes/{nodeName}")
    public ResponseEntity<List<AuditLog>> getNodeAudit(@PathVariable String nodeName) {
        return ResponseEntity.ok(getAuditLogsUseCase.execute(nodeName));
    }

    @GetMapping("/records/{recordId}")
    public ResponseEntity<List<AuditLog>> getRecordAudit(@PathVariable UUID recordId) {
        return ResponseEntity.ok(getAuditLogsUseCase.executeByRecord(recordId));
    }
}