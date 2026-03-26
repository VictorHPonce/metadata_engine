package com.devexup.fabric.infrastructure.config;

import com.devexup.fabric.infrastructure.persistence.jpa.TenantEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeDefinitionRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.NodeDefinitionEntity;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataTenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SpringDataTenantRepository tenantRepository;
    private final SpringDataNodeDefinitionRepository definitionRepository;

    public static final UUID SYSTEM_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Crear el Tenant de Sistema si no existe
        if (!tenantRepository.existsById(SYSTEM_TENANT_ID)) {
            TenantEntity systemTenant = TenantEntity.builder()
                    .id(SYSTEM_TENANT_ID)
                    .name("Sistema Scraper")
                    .subdomain("admin")
                    .active(true)
                    .build();
            tenantRepository.save(systemTenant);
            System.out.println(">>> [INIT] Tenant de Sistema creado.");
        }

        // 2. Crear Definiciones Base (RepoTrending y NoticiaTech) para el Sistema
        createBaseDefinitions();
    }

    private void createBaseDefinitions() {
        // 1. Nodo para Resultados de GitHub
        ensureDefinitionExists("RepoTrending",
                "{\"type\": \"object\", \"required\": [\"nombre\", \"link\"], \"properties\": {\"nombre\": {\"type\": \"string\"}, \"link\": {\"type\": \"string\"}, \"lenguaje\": {\"type\": \"string\"}, \"estrellas\": {\"type\": \"string\"}}}");

        // 2. Nodo para Noticias
        ensureDefinitionExists("NoticiaTech",
                "{\"type\": \"object\", \"required\": [\"titulo\", \"link\"], \"properties\": {\"titulo\": {\"type\": \"string\"}, \"link\": {\"type\": \"string\"}, \"fuente\": {\"type\": \"string\"}, \"tipo\": {\"type\": \"string\"}}}");

        // 3. Nodo para Logs (Auditoría)
        ensureDefinitionExists("ScraperLog",
                "{\"type\": \"object\", \"properties\": {\"configName\": {\"type\": \"string\"}, \"status\": {\"type\": \"string\"}, \"message\": {\"type\": \"string\"}}}");

        // 4. Nodo de CONFIGURACIÓN (El que manda)
        ensureDefinitionExists("ConfigScraper",
                "{\"type\": \"object\", \"required\": [\"keyword\", \"target\"], \"properties\": {\"keyword\": {\"type\": \"string\"}, \"target\": {\"type\": \"string\"}, \"active\": {\"type\": \"boolean\"}}}");
    }

    private void ensureDefinitionExists(String name, String schema) {
        if (definitionRepository.findByTenantIdAndName(SYSTEM_TENANT_ID, name).isEmpty()) {
            NodeDefinitionEntity entity = NodeDefinitionEntity.builder()
                    .id(UUID.randomUUID())
                    .tenantId(SYSTEM_TENANT_ID)
                    .name(name)
                    .schema(schema)
                    .version(1)
                    .build();
            definitionRepository.save(entity);
            log.info(">>> [INIT] Definición {} creada.", name);
        }
    }
}