# Etapa de build: Usa Maven con OpenJDK 17 (LTS)
FROM maven:3.8.6-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa de runtime: Usa OpenJDK 17 slim (ligera y disponible)
FROM openjdk:17-jdk-slim
COPY --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar

# Render asigna un puerto dinámico; expón 8080 por defecto
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
