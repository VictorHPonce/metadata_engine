package com.devexup.fabric.domain.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NodePermission {
    private final UUID id;
    private final UUID tenantId;
    private final String role;
    private final String userEmail;
    private final String nodeName; 
    private final PermissionType permission; 
}
