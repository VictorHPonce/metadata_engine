package com.devexup.fabric.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devexup.fabric.application.dto.DashboardSummaryDTO;
import com.devexup.fabric.application.usecase.GetDashboardSummaryUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final GetDashboardSummaryUseCase getDashboardSummaryUseCase;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(getDashboardSummaryUseCase.execute());
    }
}
