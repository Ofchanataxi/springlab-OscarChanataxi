# Etapa 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src


# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Variables de versión
ARG APP_VERSION=dev
ARG BUILD_NUMBER=0
ARG COMMIT_HASH=unknown

ENV APP_VERSION=${APP_VERSION}
ENV BUILD_NUMBER=${BUILD_NUMBER}
ENV COMMIT_HASH=${COMMIT_HASH}

# Copiar JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Inicio
ENTRYPOINT ["java", "-jar", "app.jar"]