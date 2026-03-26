# Stage 1: Construcción (JDK completo)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 1. Copiamos solo los archivos de Gradle primero para aprovechar la caché de capas de Docker
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 2. Descargamos dependencias. Si no cambias el build.gradle, esta capa se cachea y el build es volador
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 3. Copiamos el código fuente y compilamos el jar
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Ejecución (JRE ligero - Solo lo necesario para correr la app)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalamos curl para el Healthcheck (opcional pero recomendado para Docker/Traefik)
RUN apk add --no-label --no-cache curl

# Copiamos el jar generado en el Stage 1
# Usamos un comodín para el nombre del jar para que no falle si cambias la versión en Gradle
COPY --from=build /app/build/libs/*.jar app.jar

# Configuración de memoria optimizada para Java 21 en contenedores
# -XX:+UseG1GC: El recolector de basura más eficiente para microservicios
# -XX:+ExitOnOutOfMemoryError: Para que el contenedor se reinicie si se queda sin RAM
ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

# Usamos ENTRYPOINT con formato de lista para que Java reciba correctamente las señales de apagado de Docker
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]