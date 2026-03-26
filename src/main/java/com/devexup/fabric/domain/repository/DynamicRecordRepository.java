package com.devexup.fabric.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.devexup.fabric.domain.model.DynamicRecord;

public interface DynamicRecordRepository {
    DynamicRecord save(DynamicRecord record);

    Optional<DynamicRecord> findById(UUID id);

    List<DynamicRecord> findAllByNodeName(String nodeName);

    Optional<DynamicRecord> findFirstByDefinitionNameOrderByCreatedAtDesc(String definitionName);

    List<DynamicRecord> search(String nodeName, String field, String operator, Object value);

    boolean existsByLink(String nodeName, String link);

    void softDelete(UUID id); // Marca deletedAt con la fecha actual

    void restore(UUID id); // <--- AÑADIR ESTO: Pone deletedAt en null

    List<DynamicRecord> findAllActiveByNodeName(String nodeName);

    List<DynamicRecord> findAllDeletedByNodeName(String nodeName);

    Long countByNodeNameAndTenantIdAndDeletedAtIsNotNull(String nodeName, UUID tenantId);

    DynamicRecord update(DynamicRecord record);
}