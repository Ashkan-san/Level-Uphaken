# Builder
FROM openjdk:11-jdk-slim AS builder
ADD src/ /app/src/
ADD gradle/ /app/gradle/
ADD build.gradle /app/

COPY gradlew /app/
RUN chmod +x /app/gradlew
WORKDIR /app
RUN ./gradlew bootJar

# Runner
FROM openjdk:11-jre-slim AS runner
LABEL maintainer="Philip Gisella <philip.gisella@haw-hamburg.de>"
EXPOSE 8080
WORKDIR /app
COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar ./
ENTRYPOINT ["java", "-server", "-jar", "backend-0.0.1-SNAPSHOT.jar", "--spring.config.location=classpath:/application-with-db.yml"]
