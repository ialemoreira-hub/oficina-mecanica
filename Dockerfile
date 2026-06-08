FROM eclipse-temurin:21-jdk-alpine AS build 
WORKDIR /app 
RUN apk add --no-cache maven 
COPY . . 
RUN mvn clean package -DskipTests 
FROM eclipse-temurin:21-jre-alpine 
WORKDIR /app 
COPY --from=build /app/target/oficina-mecanica-1.0.0.jar app.jar 
EXPOSE 8080 
CMD ["java", "-jar", "app.jar"] 
