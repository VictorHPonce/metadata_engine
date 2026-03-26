-- Habilitar extensión para UUIDs si no está (dependiendo de tu versión de Postgres)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Borrar en orden inverso por las Foreign Keys
DROP TABLE IF EXISTS node_edges;
DROP TABLE IF EXISTS node_records;
DROP TABLE IF EXISTS node_definitions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tenants;

-- 1. TENANTS: El cliente (Pizzería, Logística, etc.)
CREATE TABLE tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(100) UNIQUE, 
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. USERS: Personal del Tenant
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(50) DEFAULT 'USER', -- ADMIN, USER, VIEWER
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. NODE DEFINITIONS (La Receta) - Ahora ligada al Tenant
CREATE TABLE node_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    schema JSONB NOT NULL,
    ui_config JSONB,
    behavior_config JSONB,
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    -- El nombre del nodo debe ser único PERO solo dentro de la empresa
    UNIQUE(tenant_id, name) 
);

-- 4. NODE RECORDS (Los Datos)
CREATE TABLE node_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    definition_id UUID NOT NULL REFERENCES node_definitions(id) ON DELETE CASCADE,
    data JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- 5. NODE EDGES (Relaciones de Grafo)
CREATE TABLE node_edges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    source_id UUID NOT NULL REFERENCES node_records(id) ON DELETE CASCADE,
    target_id UUID NOT NULL REFERENCES node_records(id) ON DELETE CASCADE,
    relation_type VARCHAR(50) NOT NULL, -- 'PERTENECE_A', 'ASIGNADO_A'
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ÍNDICES GIN PARA RENDIMIENTO
CREATE INDEX idx_node_records_data ON node_records USING GIN (data);
CREATE INDEX idx_node_definitions_tenant ON node_definitions(tenant_id);
CREATE INDEX idx_node_records_tenant ON node_records(tenant_id);

-- CONFIGURACIÓN DE SEGURIDAD (RLS)
-- Esto activa el muro de seguridad en las tablas de datos
ALTER TABLE node_records ENABLE ROW LEVEL SECURITY;
ALTER TABLE node_definitions ENABLE ROW LEVEL SECURITY;
ALTER TABLE node_edges ENABLE ROW LEVEL SECURITY;

-- POLÍTICA: Solo se ven datos si el tenant_id coincide con el seteado en la sesión
CREATE POLICY tenant_isolation_policy ON node_records
    USING (tenant_id = current_setting('app.current_tenant')::uuid);

CREATE POLICY tenant_isolation_policy ON node_definitions
    USING (tenant_id = current_setting('app.current_tenant')::uuid);

CREATE POLICY tenant_isolation_policy ON node_edges
    USING (tenant_id = current_setting('app.current_tenant')::uuid);

-- Índice de unicidad para evitar duplicados de links por Tenant y Nodo
CREATE UNIQUE INDEX idx_unique_repo_link_tenant 
ON node_records (tenant_id, (data->>'link')) 
WHERE (definition_id = '4afed9cb-5933-4ab6-be21-1ec2b4e50eda');

-- 1. Insertar el Tenant de Sistema por defecto (si no quieres depender solo del DataInitializer)
INSERT INTO tenants (id, name, subdomain, active) 
VALUES ('00000000-0000-0000-0000-000000000000', 'Sistema Fabric', 'system', true)
ON CONFLICT (id) DO NOTHING;

-- 2. IMPORTANTE: Eximir a la tabla de usuarios de RLS si es necesario para el login
-- O mejor aún, asegúrate de que el login se ejecute con un usuario de BD que pueda ver los emails
-- para validar la contraseña antes de setear el tenant_id.
ALTER TABLE users DISABLE ROW LEVEL SECURITY; 
-- Nota: La tabla 'users' no suele llevar RLS porque necesitas buscar el email 
-- ANTES de saber qué tenant es el usuario.



-- -- Borrar tablas si existen para desarrollo (Cuidado en prod)
-- DROP TABLE IF EXISTS node_records;
-- DROP TABLE IF EXISTS node_definitions;

-- -- 1. Tabla de Metadatos (La Receta)
-- CREATE TABLE node_definitions (
--     id UUID PRIMARY KEY,
--     name VARCHAR(100) NOT NULL UNIQUE,
--     schema JSONB NOT NULL,            -- El JSON Schema de validación
--     ui_config JSONB,                  -- Configuración para Angular
--     behavior_config JSONB,            -- Reglas de negocio extras
--     version INT NOT NULL DEFAULT 1,
--     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
-- );

-- -- 2. Tabla de Datos (El Plato)
-- CREATE TABLE node_records (
--     id UUID PRIMARY KEY,
--     definition_id UUID NOT NULL REFERENCES node_definitions(id) ON DELETE CASCADE,
--     data JSONB NOT NULL,              -- Los datos dinámicos reales
--     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITH TIME ZONE
-- );

-- -- Índices GIN para que PostgreSQL busque dentro del JSONB como un rayo
-- CREATE INDEX idx_node_definitions_schema ON node_definitions USING GIN (schema);
-- CREATE INDEX idx_node_records_data ON node_records USING GIN (data);