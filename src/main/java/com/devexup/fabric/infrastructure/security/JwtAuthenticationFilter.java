package com.devexup.fabric.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                DecodedJWT decodedJWT = jwtProvider.verifyAndDecode(token);
                String email = decodedJWT.getSubject();
                
                // 1. Extraer el Rol (ROLE_ADMIN)
                String role = decodedJWT.getClaim("role").asString();
                
                // 2. Extraer los Permisos (Pedidos:WRITE, etc)
                String perms = decodedJWT.getClaim("perms").asString();

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                if (perms != null && !perms.isEmpty()) {
                    Arrays.stream(perms.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .forEach(authorities::add);
                }

                // 3. Inyectar en el contexto de Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Si el token falla, limpiamos el contexto
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}