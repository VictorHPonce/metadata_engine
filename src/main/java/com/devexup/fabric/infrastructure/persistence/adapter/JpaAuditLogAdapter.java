package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.AuditLog;
import com.devexup.fabric.domain.repository.AuditLogRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataAuditLogRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.AuditLogMapper; // Inyectar
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaAuditLogAdapter implements AuditLogRepository {

    private final SpringDataAuditLogRepository jpaRepo;
    private final AuditLogMapper mapper;

    @Override
    public void save(AuditLog log) {
        jpaRepo.save(mapper.toEntity(log));
    }

    @Override
    public List<AuditLog> findByNodeName(String nodeName) {
        return jpaRepo.findByNodeNameOrderByTimestampDesc(nodeName).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByRecordId(UUID recordId) {
        return jpaRepo.findByRecordIdOrderByTimestampDesc(recordId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}