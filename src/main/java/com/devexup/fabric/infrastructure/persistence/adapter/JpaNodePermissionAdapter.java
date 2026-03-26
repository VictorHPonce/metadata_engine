package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.NodePermission;
import com.devexup.fabric.domain.repository.NodePermissionRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.NodePermissionEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodePermissionRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.NodePermissionMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaNodePermissionAdapter implements NodePermissionRepository {

    private final SpringDataNodePermissionRepository jpaRepo;
    private final NodePermissionMapper mapper;

    @Override
    public List<NodePermission> findByRoleAndTenantId(String role, UUID tenantId) {
        return jpaRepo.findByRoleAndTenantId(role, tenantId).stream()
                .map(mapper::toDomain) 
                .toList();
    }

    @Override
    public List<NodePermission> findByUserEmailAndTenantId(String email, UUID tenantId) {
        return jpaRepo.findByUserEmailAndTenantId(email, tenantId).stream()
                .map(mapper::toDomain) 
                .toList();
    }

    @Override
    public NodePermission save(NodePermission permission) {
        NodePermissionEntity entity = mapper.toEntity(permission);
        return mapper.toDomain(jpaRepo.save(entity));
    }

}