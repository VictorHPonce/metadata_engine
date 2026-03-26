package com.devexup.fabric.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service // Lo convertimos en un servicio de Spring
public class JsonSchemaValidator {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    public void validateOrThrow(String schemaContent, String dataContent) {
        try {
            JsonSchema schema = schemaFactory.getSchema(schemaContent);
            JsonNode node = objectMapper.readTree(dataContent);
            
            Set<ValidationMessage> errors = schema.validate(node);
            
            if (!errors.isEmpty()) {
                String errorDetails = errors.stream()
                        .map(ValidationMessage::getMessage)
                        .collect(Collectors.joining(", "));
                
                log.warn(">>> Validación de Schema fallida: {}", errorDetails);
                throw new RuntimeException("Datos no válidos según el esquema: " + errorDetails);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException("Error procesando validación: " + e.getMessage());
        }
    }
}