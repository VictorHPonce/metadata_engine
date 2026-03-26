package com.devexup.fabric.web.controller;

import com.devexup.fabric.application.dto.AuthResponse;
import com.devexup.fabric.application.dto.LoginRequest;
import com.devexup.fabric.application.dto.RefreshTokenRequest;
import com.devexup.fabric.application.dto.RegisterRequest;
import com.devexup.fabric.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Endpoint para registro de empresa
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request.getCompanyName(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(Map.of("message", "Registro de empresa exitoso"));
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}