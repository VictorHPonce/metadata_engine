package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.Tenant;
import com.devexup.fabric.domain.repository.TenantRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataTenantRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.TenantEntity;
import com.devexup.fabric.infrastructure.persistence.mapper.TenantMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaTenantRepositoryAdapter implements TenantRepository {

    private final SpringDataTenantRepository jpaRepo;
    private final TenantMapper mapper; 

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity saved = jpaRepo.save(mapper.toEntity(tenant));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }
}