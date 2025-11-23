# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy jar dari stage build
COPY --from=build /app/target/*.jar app.jar

# WAJIB: paksa Java pakai IPv4 (ini yang selama ini bikin Railway 502)
ENV JAVA_TOOL_OPTIONS="-Djava.net.preferIPv4Stack=true"

# Railway akan inject env PORT otomatis. Kalau tidak ada, fallback ke 8081
EXPOSE 8081

# Paling simpel & paling pasti jalan di Railway
ENTRYPOINT ["java", "-jar", "app.jar"]