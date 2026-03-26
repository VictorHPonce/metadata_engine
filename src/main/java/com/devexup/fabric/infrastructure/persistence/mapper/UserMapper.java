package com.devexup.fabric.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.devexup.fabric.domain.model.User;
import com.devexup.fabric.infrastructure.persistence.jpa.UserEntity;

@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .email(entity.getEmail())
                .password(entity.getPasswordHash())
                .role(entity.getRole())
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .email(domain.getEmail())
                .passwordHash(domain.getPassword())
                .role(domain.getRole())
                .build();
    }
}
