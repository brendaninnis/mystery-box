# Stage 1: Build the Fat JAR
FROM amazoncorretto:21 AS build
WORKDIR /
COPY . .
RUN ./gradlew :deployBackend

# Stage 2: Production Runtime
FROM amazoncorretto:21-alpine
WORKDIR /app

# Copy only the built JAR from the first stage
COPY --from=build /build/deploy/backend/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
