package com.devexup.fabric.web.controller;

import com.devexup.fabric.application.usecase.GetTrashRecordsUseCase;
import com.devexup.fabric.application.usecase.RestoreRecordUseCase;
import com.devexup.fabric.domain.model.DynamicRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nodes/{nodeName}/trash")
@RequiredArgsConstructor
public class NodeTrashController {

    private final GetTrashRecordsUseCase getTrashRecordsUseCase;
    private final RestoreRecordUseCase restoreRecordUseCase;

    @GetMapping
    public ResponseEntity<List<DynamicRecord>> getTrash(@PathVariable String nodeName) {
        return ResponseEntity.ok(getTrashRecordsUseCase.execute(nodeName));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id) {
        restoreRecordUseCase.execute(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTrashCount(@PathVariable String nodeName) {
        // El UseCase debe contar los registros donde deleted_at IS NOT NULL para ese
        // tenant
        return ResponseEntity.ok(getTrashRecordsUseCase.count(nodeName));
    }
}