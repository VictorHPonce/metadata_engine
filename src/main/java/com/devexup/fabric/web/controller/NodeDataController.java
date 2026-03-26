package com.devexup.fabric.web.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devexup.fabric.application.usecase.GetNodeAggregationUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/nodes")
@RequiredArgsConstructor
public class NodeDataController {

    private final GetNodeAggregationUseCase getNodeAggregationUseCase;

    @GetMapping("/{nodeName}/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @PathVariable String nodeName,
            @RequestParam String field,
            @RequestParam(defaultValue = "SUM") String op) {
        
        BigDecimal value = getNodeAggregationUseCase.execute(nodeName, field, op);
        
        return ResponseEntity.ok(Map.of(
            "node", nodeName,
            "field", field,
            "operation", op,
            "result", value
        ));
    }
}