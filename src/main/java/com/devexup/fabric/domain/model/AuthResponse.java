package com.devexup.fabric.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;  // El que tiene los permisos (Corta vida)
    private String refreshToken; // El que permite renovar (Larga vida)
    private String email;
    private String role;
    private List<String> permissions; // Para que Angular dibuje el menú
}
