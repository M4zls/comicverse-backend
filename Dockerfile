# ───────────────────────────────
# STAGE 1: Build con Gradle
# ───────────────────────────────
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos los archivos de Gradle primero (para cachear dependencias)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Descargar dependencias sin compilar todo
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies || true

# Ahora copiamos el código fuente
COPY src src

# Compilar el proyecto (sin tests para acelerar)
RUN ./gradlew --no-daemon clean build -x test

# ───────────────────────────────
# STAGE 2: Imagen final
# ───────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

# Railway usa el puerto 8080 por defecto, pero será configurado por variable de entorno
EXPOSE ${PORT:-8080}

# Copiar el jar compilado
COPY --from=build /app/build/libs/*.jar app.jar

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run con optimizaciones para Railway
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
