FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install package -Dmaven.test.skip

FROM eclipse-temurin:17-jdk-alpine
COPY --from=builder /app/target/resmenu-0.0.1-SNAPSHOT.jar app.jar
CMD [ "java", "-jar", "app.jar" ]
