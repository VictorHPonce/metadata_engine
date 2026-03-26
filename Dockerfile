# Stage 1: Construcción
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Ejecución (Con soporte para Playwright)
FROM mcr.microsoft.com/playwright/java:v1.49.0-noble
WORKDIR /app

# Instalamos Java 21 en la imagen de Playwright (que viene con Node)
RUN apt-get update && apt-get install -y openjdk-21-jre && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/*.jar app.jar

# Variables para Playwright y Memoria
ENV JAVA_OPTS="-Xms128m -Xmx512m"
ENV PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=0

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


# # Stage 1: Construcción (JDK 21 + Gradle)
# FROM eclipse-temurin:21-jdk-alpine AS build
# WORKDIR /app

# # Copiamos los archivos de configuración de Gradle
# COPY gradlew .
# COPY gradle gradle
# COPY build.gradle .
# COPY settings.gradle .

# # Permiso de ejecución y descarga de dependencias (cacheable)
# RUN chmod +x gradlew
# RUN ./gradlew dependencies --no-daemon

# # Copiamos el código fuente y compilamos
# COPY src src
# RUN ./gradlew bootJar -x test --no-daemon

# # Stage 2: Ejecución (JRE 21 ligero)
# FROM eclipse-temurin:21-jre-alpine
# WORKDIR /app

# # Copiamos el jar
# COPY --from=build /app/build/libs/*.jar app.jar

# # Ajuste de memoria para Java 21
# ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC"

# EXPOSE 8080
# ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]