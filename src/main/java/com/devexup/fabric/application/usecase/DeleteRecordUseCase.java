package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.application.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteRecordUseCase {

    private final DynamicRecordRepository recordRepository;
    private final AuditService auditService;

    @Transactional
    public void execute(UUID id) {
        // 1. Buscar el registro para saber a qué nodo pertenece
        DynamicRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        // 2. Ejecutar el Soft Delete en el repositorio
        recordRepository.softDelete(id);

        // 3. Registrar en la auditoría
        auditService.log("DELETE", record.getNodeName(), id, "Registro enviado a la papelera");
    }
}