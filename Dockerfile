# ───────────────────────────────
# STAGE 1: Build con Gradle
# ───────────────────────────────
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copiamos los archivos de Gradle primero (para cachear dependencias
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Descargar dependencias sin compilar todo
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies || true

# Ahora copiamos el código fuente
COPY src src

# Compilar el proyecto
RUN ./gradlew --no-daemon clean build

# ───────────────────────────────
# STAGE 2: Imagen final
# ───────────────────────────────
FROM eclipse-temurin:17-jre

WORKDIR /app

# Railway usa el puerto 8080 por defecto
EXPOSE 8080

# Copiar el jar compilado
COPY --from=build /app/build/libs/*.jar app.jar

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
