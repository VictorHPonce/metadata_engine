package com.devexup.fabric.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.UUID;

public class SecurityUtils {

    public static UUID getTenantId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Map) {
            // Suponiendo que tu JwtFilter mete el tenantId en los detalles
            return UUID.fromString(((Map<?, ?>) auth.getDetails()).get("tenantId").toString());
        }
        // Fallback: Si usas CustomUserDetails, extráelo de ahí
        return null; 
    }
}