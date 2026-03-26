package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataTenantRepository extends JpaRepository<TenantEntity, UUID> {
    // Aquí puedes añadir findBySubdomain si lo necesitas en el futuro
}