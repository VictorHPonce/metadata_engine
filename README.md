# Fabric | Metadata-Driven Engine 🚀

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)

Fabric es un motor de backend avanzado basado en el paradigma **Metadata-Driven**. Permite la creación y gestión de entidades dinámicas sin necesidad de modificar el esquema de la base de datos en tiempo de ejecución.

> **Showroom Note:** Este repositorio es un espejo público. El desarrollo y despliegue automático se gestiona desde una infraestructura privada de **Gitea**.

## 🏗️ Arquitectura del Sistema

El proyecto implementa una **Arquitectura Hexagonal (Ports & Adapters)** combinada con **Clean Architecture**, asegurando que el dominio esté desacoplado de la infraestructura.

### Estructura de Capas:
* **`domain`**: El núcleo. Contiene los modelos (`NodeDefinition`, `DynamicRecord`) y las interfaces de los repositorios. Es código puro sin dependencias de frameworks.
* **`application`**: Contiene los casos de uso (`RegisterNodeUseCase`, `SubmitRecordUseCase`) y DTOs. Coordina la lógica de negocio.
* **`infrastructure`**: Implementaciones técnicas. Incluye la persistencia (JPA/JDBC), seguridad (JWT), y clientes externos (Telegram API).
* **`web`**: Controladores REST organizados por dominios de acción.

## 🛠️ Capacidades Técnicas
* **Multi-Tenancy**: Aislamiento de datos mediante `TenantContext`.
* **Dynamic Modeling**: Definición de nodos y esquemas mediante JSON Schema.
* **JWT Security**: Flujo completo de autenticación con Refresh Tokens.
* **Audit System**: Registro automático de cambios en registros dinámicos.
* **Automated CI/CD**: Pipeline nativo en Gitea con despliegue automático en VPS mediante Docker y Traefik.

## 📦 Despliegue Automatizado

Este proyecto utiliza un flujo **GitOps**:
1.  **Push a Gitea**: Dispara una Action de construcción.
2.  **Multi-Stage Build**: Genera una imagen Docker ligera basada en Alpine (JRE 21).
3.  **Zero-Downtime Deploy**: Docker Compose actualiza el servicio en el VPS.
4.  **Reverse Proxy**: **Traefik** gestiona el tráfico HTTPS y los certificados SSL automáticamente.

---
*Desarrollado por [Victor Ponce](https://victorponce.dev) — Senior Backend & Infrastructure Engineer.*