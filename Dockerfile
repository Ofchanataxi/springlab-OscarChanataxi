# Etapa 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copiar archivos de Gradle
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copiar código fuente
COPY src ./src

# Construir la aplicación (sin ejecutar tests)
RUN ./gradlew clean build -x test

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Argumentos de build para versionado
ARG APP_VERSION=dev
ARG BUILD_NUMBER=0
ARG COMMIT_HASH=unknown

# Convertir argumentos en variables de entorno
ENV APP_VERSION=${APP_VERSION}
ENV BUILD_NUMBER=${BUILD_NUMBER}
ENV COMMIT_HASH=${COMMIT_HASH}

# Copiar el JAR de la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]