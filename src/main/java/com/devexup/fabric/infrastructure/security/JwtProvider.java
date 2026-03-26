package com.devexup.fabric.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${app.jwt.secret:mi_clave_secreta_muy_larga}")
    private String secret;

    // Aumentamos la seguridad a HMAC512 para producción
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }

    // En JwtProvider.java

    public String generateToken(User user, List<NodePermission> permissions) {
        // Formateamos permisos como "Nodo:Accion"
        String permsClaim = permissions.stream()
                .map(p -> p.getNodeName() + ":" + p.getPermission().name())
                .collect(Collectors.joining(","));

        // Spring Security espera "ROLE_ADMIN"
        String springFriendlyRole = "ROLE_" + user.getRole();

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("tenantId", user.getTenantId().toString())
                .withClaim("role", springFriendlyRole) // Ahora viajará como ROLE_ADMIN
                .withClaim("perms", permsClaim)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(getAlgorithm());
    }

    public DecodedJWT verifyAndDecode(String token) {
        return JWT.require(getAlgorithm()).build().verify(token);
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("isRefresh", true) // Marca para saber que no es un token de acceso
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 604800000)) // 7 días
                .sign(Algorithm.HMAC512(secret));
    }
}