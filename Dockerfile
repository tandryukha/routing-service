FROM maven:3.8.5-openjdk-17-slim as build
COPY . application
WORKDIR /application
RUN mvn package -Dmaven.test.skip=true

###Image for run
FROM openjdk:17-jdk-slim as run-image
ARG JAR_FILE=/application/target/routing-service-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]