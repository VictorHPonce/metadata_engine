package com.devexup.fabric.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormulaService {
    private final ObjectMapper mapper;

    public String calculateFields(String jsonData, Map<String, String> formulas) {
        try {
            ObjectNode root = (ObjectNode) mapper.readTree(jsonData);

            formulas.forEach((targetField, expression) -> {
                // Ejemplo de expresión: "total * 1.21"
                if (expression.contains("total * 1.21")) {
                    double base = root.path("total").asDouble();
                    root.put(targetField, Math.round((base * 1.21) * 100.0) / 100.0);
                }
                // Aquí puedes añadir más "reglas" o usar una librería de expresiones
            });

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            log.error("Error en cálculo: {}", e.getMessage());
            return jsonData;
        }
    }
}