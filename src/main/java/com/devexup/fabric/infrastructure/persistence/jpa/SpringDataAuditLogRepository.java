package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
    List<AuditLogEntity> findByNodeNameOrderByTimestampDesc(String nodeName);
    List<AuditLogEntity> findByRecordIdOrderByTimestampDesc(UUID recordId);
}