package com.devexup.fabric.infrastructure.security;

import com.devexup.fabric.domain.model.PermissionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean hasAccess(String nodeName, PermissionType requiredPermission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        // Si es ADMIN de la empresa (Tenant Admin), tiene permiso total
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        // Buscamos el permiso específico "Nodo:Accion" inyectado desde el JWT
        String requiredGrant = nodeName + ":" + requiredPermission.name();
        
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(requiredGrant));
    }
}