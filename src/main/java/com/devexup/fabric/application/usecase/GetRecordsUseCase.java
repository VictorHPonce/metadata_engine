package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.infrastructure.config.TenantContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRecordsUseCase {

    private final DynamicRecordRepository recordRepository;

    public List<DynamicRecord> execute(String nodeName) {
        log.info(">>> [GET] Consultando registros del nodo {} para el Tenant {}", 
                 nodeName, TenantContext.getTenantId());
        
        // Gracias al RLS de Postgres (SET app.current_tenant), 
        // el recordRepository ya no devolverá datos de otros clientes.
        return recordRepository.findAllByNodeName(nodeName);
    }

    public List<DynamicRecord> executeWithFilter(String nodeName, String field, String operator, String value) {
        log.info(">>> [SEARCH] Tenant {} filtrando {} por {} {} {}", 
                 TenantContext.getTenantId(), nodeName, field, operator, value);
        return recordRepository.search(nodeName, field, operator, value);
    }
}