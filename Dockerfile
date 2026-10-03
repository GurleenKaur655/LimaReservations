# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
ARG MODULE
RUN mvn clean package -DskipTests -pl ${MODULE} -am

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
ARG MODULE
ARG PORT=8080
COPY --from=build /app/${MODULE}/target/*.jar app.jar
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]