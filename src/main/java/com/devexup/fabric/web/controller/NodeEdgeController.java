package com.devexup.fabric.web.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devexup.fabric.application.usecase.ConnectNodesUseCase;
import com.devexup.fabric.domain.model.NodeEdge;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/edges")
@RequiredArgsConstructor
public class NodeEdgeController {

    private final ConnectNodesUseCase connectNodesUseCase;

    @PostMapping
    public ResponseEntity<NodeEdge> connect(@RequestBody EdgeRequest request) {
        NodeEdge result = connectNodesUseCase.execute(
                request.getSourceId(),
                request.getTargetId(),
                request.getRelationType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Data
    static class EdgeRequest {
        private UUID sourceId;
        private UUID targetId;
        private String relationType;
    }
}
