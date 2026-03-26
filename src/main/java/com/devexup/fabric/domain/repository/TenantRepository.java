package com.devexup.fabric.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.devexup.fabric.domain.model.Tenant;

public interface TenantRepository {
    Tenant save(Tenant tenant);

    Optional<Tenant> findById(UUID id);
}
