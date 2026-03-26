package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.infrastructure.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTrashRecordsUseCase {
    private final DynamicRecordRepository recordRepository;

    public List<DynamicRecord> execute(String nodeName) {
        // Llama al método que implementamos en el Adapter hace un momento
        return recordRepository.findAllDeletedByNodeName(nodeName);
    }

    public Long count(String nodeName) {
    // Aquí invocas a tu repositorio filtrando por nodeName, 
    // por el tenantId del contexto de seguridad y donde deleted_at no sea null.
    return recordRepository.countByNodeNameAndTenantIdAndDeletedAtIsNotNull(nodeName, SecurityUtils.getTenantId());
}
}