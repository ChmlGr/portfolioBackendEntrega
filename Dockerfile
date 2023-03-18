FROM maven:3-amazoncorretto-19 AS builder

WORKDIR /tmp

COPY pom.xml .

COPY src src

RUN mvn clean package -DskipTests

FROM amazoncorretto:19-alpine-jdk 

WORKDIR /app

COPY --from=builder /tmp/target/app-be.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app-be.jar"]