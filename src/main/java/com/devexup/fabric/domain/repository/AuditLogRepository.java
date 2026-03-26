package com.devexup.fabric.domain.repository;

import com.devexup.fabric.domain.model.AuditLog;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository {
    void save(AuditLog log);
    List<AuditLog> findByNodeName(String nodeName);
    List<AuditLog> findByRecordId(UUID recordId);
}