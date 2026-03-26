package com.devexup.fabric.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshToken {
    private final UUID id;
    private final UUID userId;
    private final String token;
    private final Instant expiryDate;
}
