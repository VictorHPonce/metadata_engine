package com.devexup.fabric.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.devexup.fabric.domain.model.Tenant;
import com.devexup.fabric.infrastructure.persistence.jpa.TenantEntity;

@Component
public class TenantMapper {
    public Tenant toDomain(TenantEntity entity) {
        if (entity == null) return null;
        return Tenant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .subdomain(entity.getSubdomain())
                .active(entity.isActive())
                .build();
    }

    public TenantEntity toEntity(Tenant domain) {
        if (domain == null) return null;
        return TenantEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .subdomain(domain.getSubdomain())
                .active(domain.isActive())
                .build();
    }
}
