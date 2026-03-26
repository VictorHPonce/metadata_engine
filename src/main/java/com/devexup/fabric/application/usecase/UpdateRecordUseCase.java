package com.devexup.fabric.application.usecase;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRecordUseCase {

    private final DynamicRecordRepository recordRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public DynamicRecord executePartial(UUID id, Map<String, Object> updates) {
        // 1. Buscar el registro actual
        DynamicRecord existingRecord = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        try {
            // 2. Leer el String JSON actual y convertirlo a Map
            Map<String, Object> currentData = objectMapper.readValue(
                existingRecord.getData(), 
                new TypeReference<Map<String, Object>>() {}
            );

            // 3. Fusionar los cambios (PATCH)
            currentData.putAll(updates);

            // 4. Convertir el Map resultante de nuevo a String JSON
            String updatedJson = objectMapper.writeValueAsString(currentData);

            // 5. Crear una COPIA del registro usando toBuilder con el nuevo JSON
            DynamicRecord updatedRecord = existingRecord.toBuilder()
                    .data(updatedJson)
                    .updatedAt(OffsetDateTime.now())
                    .build();

            // 6. Persistir
            return recordRepository.update(updatedRecord);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el JSON del registro", e);
        }
    }
}