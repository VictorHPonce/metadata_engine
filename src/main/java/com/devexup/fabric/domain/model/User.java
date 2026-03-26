package com.devexup.fabric.domain.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private final UUID id;
    private final UUID tenantId;
    private final String email;
    private final String password;
    private final String role;
}