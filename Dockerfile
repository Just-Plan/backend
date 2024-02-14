# Gradle과 JDK를 포함하는 베이스 이미지
FROM gradle:7.3.1-jdk17 as build

# 소스 코드 복사
COPY . /home/gradle/src
WORKDIR /home/gradle/src

# 빌드 실행
RUN gradle build -x test

# 런타임 이미지
FROM openjdk:17.0-slim
WORKDIR /app

# 빌드 아티팩트 복사
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
