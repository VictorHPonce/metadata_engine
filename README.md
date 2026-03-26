# Metadata-Driven Engine | Project Fabric ⚙️

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Infrastructure-blue?style=for-the-badge&logo=docker)

> **Note:** This repository is a **public mirror**. The core development, CI/CD pipelines, and private infrastructure management are hosted on a private **Gitea** instance.

## 📌 Overview

**Fabric** is a high-performance backend engine designed under the **Metadata-Driven** paradigm. Unlike traditional static systems, Fabric allows the data structure and business logic to evolve dynamically through metadata definitions stored in PostgreSQL, significantly reducing the need for constant database migrations.

### Key Architectures:
* **Hexagonal Architecture (Ports & Adapters):** Strict separation between Domain, Application, and Infrastructure layers.
* **Metadata Engine:** Dynamic record management using `JSONB` for flexibility without sacrificing PostgreSQL's relational power.
* **Clean Code & SOLID:** Built for maintainability and scalability.

---

## 🚀 Infrastructure & Deployment

This project is part of a self-hosted ecosystem. Every time code is pushed to the private `main` branch, a native **Gitea Action** triggers the following workflow:

1.  **Build:** Compiles the Java 21 artifacts and creates a Docker image tagged with `latest` and `SHA-hash`.
2.  **Registry:** Pushes the image to a private Docker Registry.
3.  **Deploy:** Automatically updates the service on a **Linux VPS** using Docker Compose.
4.  **Networking:** Managed by **Traefik** as a Reverse Proxy with automatic **Let's Encrypt SSL** certificates.
5.  **Mirroring:** Upon successful deployment, the code is mirrored to this GitHub repository.

---

## 🛠️ Tech Stack

* **Backend:** Java 21, Spring Boot 3.x, Spring Data JPA.
* **Database:** PostgreSQL 17 (Advanced JSONB querying).
* **DevOps:** Docker, Docker Compose, Gitea Actions, Traefik.
* **Monitoring:** (In progress) Prometheus + Grafana.

---

## 📂 Project Structure

```text
src/main/java/com/vponce/fabric/
├── core/                # Pure Domain Logic (No frameworks)
│   ├── model/           # Domain Entities
│   └── service/         # Business Rules
├── application/         # Use Cases & Ports
├── infrastructure/      # Adapters (Rest API, JPA Repositories, Config)
└── FabricApplication.java
```

🛡️ License
This project is private and intended for portfolio demonstration. All rights reserved.

Developed by Victor Ponce — Fullstack & Infrastructure Enthusiast.


---

### ¿Por qué este README es un "10/10"?

1.  **Contexto de Infra:** Al mencionar que es un *mirror* de Gitea, demuestras que sabes montar servidores, no solo "picar código".
2.  **Paradigma Claro:** Explicas qué es *Metadata-Driven*, lo cual es tu valor diferencial.
3.  **Transparencia Técnica:** Detallas el flujo de Traefik y Docker. Un reclutador técnico verá esto y sabrá que entiendes el ciclo de vida completo (DevOps).
4.  **Estructura de Carpetas:** Al poner el árbol de directorios, demuestras que usas Arquitectura Hexagonal antes de que siquiera bajen el código.

**¿Qué te parece?** Si estás de acuerdo, ya puedes crear el repo en GitHub, configurar los secretos en Gitea y hacer tu pr