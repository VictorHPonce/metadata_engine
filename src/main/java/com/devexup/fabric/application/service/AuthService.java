package com.devexup.fabric.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.domain.model.RefreshToken;
import com.devexup.fabric.domain.model.Tenant;
import com.devexup.fabric.domain.model.User;
import com.devexup.fabric.domain.repository.NodePermissionRepository;
import com.devexup.fabric.domain.repository.RefreshTokenRepository;
import com.devexup.fabric.domain.repository.TenantRepository;
import com.devexup.fabric.domain.repository.UserRepository;
import com.devexup.fabric.domain.service.PermissionAutomationService;
import com.devexup.fabric.infrastructure.security.JwtProvider;
import com.devexup.fabric.application.dto.AuthResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final TenantRepository tenantRepository;
        private final NodePermissionRepository permissionRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtProvider jwtProvider;
        private final RefreshTokenRepository refreshTokenRepository;
        private final PermissionAutomationService permissionAutomationService;

        @Transactional
        public void register(String companyName, String email, String password) {
                // 1. Crear Tenant
                Tenant newTenant = Tenant.builder()
                                .id(UUID.randomUUID())
                                .name(companyName)
                                .active(true)
                                .build();
                tenantRepository.save(newTenant);

                // 2. Crear Usuario Admin
                User newUser = User.builder()
                                .id(UUID.randomUUID())
                                .tenantId(newTenant.getId())
                                .email(email)
                                .password(passwordEncoder.encode(password))
                                .role("ADMIN")
                                .build();
                userRepository.save(newUser);

                // 3. AUTOMATIZACIÓN: Permisos iniciales para que el admin pueda usar el sistema
                permissionAutomationService.grantFullAccess(newTenant.getId(), "ADMIN", "Usuarios");
                permissionAutomationService.grantFullAccess(newTenant.getId(), "ADMIN", "Preferencias");
        }

        @Transactional
        public AuthResponse login(String email, String password) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

                if (!passwordEncoder.matches(password, user.getPassword())) {
                        throw new RuntimeException("Credenciales inválidas");
                }

                // 1. Obtenemos permisos por ROL (Cambiamos el nombre para que sea claro)
                // Usamos una implementación mutable (ArrayList) para poder combinar las listas
                List<NodePermission> allPermissions = new java.util.ArrayList<>(
                                permissionRepository.findByRoleAndTenantId(user.getRole(), user.getTenantId()));

                // 2. Obtenemos permisos por EMAIL específico
                List<NodePermission> userPermissions = permissionRepository.findByUserEmailAndTenantId(
                                user.getEmail(), user.getTenantId());

                // 3. Combinamos ambos. Ahora 'allPermissions' tiene el total.
                allPermissions.addAll(userPermissions);

                // 4. Generamos el token con la lista completa de permisos
                String accessToken = jwtProvider.generateToken(user, allPermissions);
                String refreshTokenString = jwtProvider.generateRefreshToken(user);

                // Guardar en BD
                saveRefreshToken(user.getId(), refreshTokenString);

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshTokenString)
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build();
        }

        private void saveRefreshToken(UUID userId, String tokenString) {
                // Primero limpiamos tokens viejos del usuario (opcional, para solo 1 sesión
                // activa)
                refreshTokenRepository.deleteByUserId(userId);

                RefreshToken refreshToken = RefreshToken.builder()
                                .id(UUID.randomUUID())
                                .userId(userId)
                                .token(tokenString)
                                .expiryDate(Instant.now().plusSeconds(604800)) // 7 días
                                .build();

                refreshTokenRepository.save(refreshToken);
        }

        @Transactional
        public AuthResponse refreshToken(String refreshTokenRequest) {
                // 1. Buscar el token en la BD
                RefreshToken rt = refreshTokenRepository.findByToken(refreshTokenRequest)
                                .orElseThrow(() -> new RuntimeException(
                                                "Refresh Token no encontrado en la base de datos"));

                // 2. Verificar si ha expirado
                if (rt.getExpiryDate().isBefore(Instant.now())) {
                        refreshTokenRepository.deleteByUserId(rt.getUserId());
                        throw new RuntimeException("Refresh Token expirado. Por favor, inicie sesión de nuevo");
                }

                // 3. Obtener el usuario para generar los nuevos claims
                User user = userRepository.findById(rt.getUserId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // 4. Obtener permisos actuales (por si cambiaron desde el último login)
                List<NodePermission> permissions = permissionRepository.findByRoleAndTenantId(user.getRole(),
                                user.getTenantId());

                // 5. Generar nuevos tokens (Rotación de tokens)
                String newAccessToken = jwtProvider.generateToken(user, permissions);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);

                // 6. Actualizar el Refresh Token en la base de datos (Borramos el viejo y
                // guardamos el nuevo)
                refreshTokenRepository.deleteByUserId(user.getId());
                saveRefreshToken(user.getId(), newRefreshToken);

                return AuthResponse.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken)
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build();
        }
}
