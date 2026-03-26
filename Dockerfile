# Stage 1: Construcción
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 1. Caché de dependencias
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 2. Compilación
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Corregido: Instalamos curl para que Traefik pueda verificar la salud del contenedor
RUN apk add --no-cache curl

# Copiamos el jar
COPY --from=build /app/build/libs/*.jar app.jar

# Optimización de memoria
ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]