# Gradle과 JDK를 포함하는 베이스 이미지
FROM gradle:7.6-jdk17 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build -x test

# 런타임 이미지
FROM openjdk:17.0-slim
WORKDIR /app

COPY --from=builder /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
