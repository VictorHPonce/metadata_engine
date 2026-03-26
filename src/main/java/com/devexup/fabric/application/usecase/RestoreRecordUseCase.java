package com.devexup.fabric.application.usecase;

import java.util.UUID;
import com.devexup.fabric.application.service.AuditService;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import org.springframework.stereotype.Service; // Asegúrate de usar este import de Spring
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestoreRecordUseCase {
    private final DynamicRecordRepository repository;
    private final AuditService auditService;

    @Transactional
    public void execute(UUID id) {
        repository.findById(id).ifPresent(record -> {
            // 1. Quitar la marca de borrado físicamente en la DB
            repository.restore(id);
            
            // 2. Registrar en la auditoría quién lo hizo
            // Ahora record.getNodeName() ya funcionará
            auditService.log(
                "RESTORE", 
                record.getNodeName(), 
                id, 
                "Registro recuperado desde la papelera de reciclaje"
            );
        });
    }
}