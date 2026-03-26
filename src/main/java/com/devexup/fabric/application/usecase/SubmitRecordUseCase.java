package com.devexup.fabric.application.usecase;

import com.devexup.fabric.application.service.AuditService;
import com.devexup.fabric.domain.model.DynamicRecord;
import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.DynamicRecordRepository;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import com.devexup.fabric.domain.service.FormulaService;
import com.devexup.fabric.domain.service.JsonSchemaValidator;
import com.devexup.fabric.infrastructure.config.TenantContext;
import com.devexup.fabric.infrastructure.external.notification.TelegramClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitRecordUseCase {

    private final NodeDefinitionRepository definitionRepository;
    private final DynamicRecordRepository recordRepository;
    private final TelegramClient telegramClient;
    private final ObjectMapper objectMapper;
    private final JsonSchemaValidator validator;
    private final AuditService auditService;
    private final FormulaService formulaService;

    @Transactional
    public DynamicRecord execute(String nodeName, String jsonData) {
        UUID currentTenantId = TenantContext.getTenantId();

        // 1. Obtener definición
        NodeDefinition definition = definitionRepository.findByName(nodeName)
                .orElseThrow(() -> new RuntimeException("Nodo no encontrado: " + nodeName));

        // 2. Validación JSON Schema
        validator.validateOrThrow(definition.getJsonSchema(), jsonData);

        // 3. Cálculos automáticos DINÁMICOS (Lógica Pro mejorada)
        String processedData = jsonData;
        String behaviorConfig = definition.getBehaviorConfig();

        if (behaviorConfig != null && !behaviorConfig.isEmpty()) {
            try {
                // Convertimos el String behaviorConfig en un Map de fórmulas
                java.util.Map<String, String> formulas = objectMapper.readValue(
                        behaviorConfig,
                        new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, String>>() {
                        });

                // Aplicamos las fórmulas dinámicas
                processedData = formulaService.calculateFields(jsonData, formulas);
                log.info(">>> Cálculos aplicados para el nodo {}: {}", nodeName, formulas.keySet());

            } catch (Exception e) {
                log.error(">>> Error al parsear behaviorConfig para cálculos: {}", e.getMessage());
                // No bloqueamos el guardado, pero avisamos del error
            }
        }

        // 4. Crear y guardar modelo con los datos procesados
        DynamicRecord record = DynamicRecord.builder()
                .id(UUID.randomUUID())
                .tenantId(currentTenantId)
                .nodeName(nodeName)
                .definitionId(definition.getId())
                .data(processedData)
                .build();

        DynamicRecord savedRecord = recordRepository.save(record);

        // 5. Auditoría
        auditService.log("CREATE", nodeName, savedRecord.getId(),
                "Registro creado con éxito (procesado dinámicamente)");

        // 6. Notificación asíncrona
        if ("RepoTrending".equals(nodeName)) {
            String finalData = processedData;
            CompletableFuture.runAsync(() -> sendTelegramAlert(finalData));
        }

        return savedRecord;
    }

    private void sendTelegramAlert(String jsonData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            String message = String.format(
                    "🚀 *¡Nuevo Repo!*\n\n📦 *Proyecto:* %s\n💻 *Lenguaje:* %s\n⭐ *Estrellas:* %s\n🔗 [Link](%s)",
                    jsonNode.path("nombre").asText("?"),
                    jsonNode.path("lenguaje").asText("?"),
                    jsonNode.path("estrellas").asText("0"),
                    jsonNode.path("link").asText("#"));
            telegramClient.sendMessage(message);
        } catch (Exception e) {
            log.error("Error al notificar por Telegram", e);
        }
    }
}