package com.devexup.fabric.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.devexup.fabric.domain.model.RefreshToken;
import com.devexup.fabric.infrastructure.persistence.jpa.RefreshTokenEntity;

@Component
public class RefreshTokenMapper {
    public RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) return null;
        return RefreshToken.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .expiryDate(entity.getExpiryDate())
                .build();
    }

    public RefreshTokenEntity toEntity(RefreshToken domain) {
        if (domain == null) return null;
        return RefreshTokenEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .token(domain.getToken())
                .expiryDate(domain.getExpiryDate())
                .build();
    }
}