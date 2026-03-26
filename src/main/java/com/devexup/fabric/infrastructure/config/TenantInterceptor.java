package com.devexup.fabric.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.devexup.fabric.infrastructure.security.JwtProvider;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {

    private final JdbcTemplate jdbcTemplate;
    private final JwtProvider jwtProvider; // Mantenemos tu inyección

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        UUID tenantId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                var decodedJWT = jwtProvider.verifyAndDecode(token);
                String extractedId = decodedJWT.getClaim("tenantId").asString();

                if (extractedId != null) {
                    tenantId = UUID.fromString(extractedId);
                    TenantContext.setTenantId(tenantId);
                    
                    // MEJORA DE SEGURIDAD: Usamos set_config en lugar de SET directo
                    // Esto es más robusto para Postgres
                    jdbcTemplate.execute(String.format("SELECT set_config('app.current_tenant', '%s', false)", tenantId));
                }
            } catch (Exception e) {
                log.error(">>> Error validando token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión inválida");
                return false;
            }
        }

        // Si es una ruta de API (que no sea auth) y no tenemos tenantId, bloqueamos
        if (path.startsWith("/api/") && !path.contains("/auth/") && tenantId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado: Tenant no identificado");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
        try {
            // MEJORA: Reseteamos la configuración de forma segura
            jdbcTemplate.execute("SELECT set_config('app.current_tenant', '', false)");
        } catch (Exception e) {
            log.error(">>> Error al resetear RLS: {}", e.getMessage());
        }
    }
}