# gradle:7.3.1-jdk17 이미지를 기반으로 함
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17 AS build

WORKDIR /home/gradle/project

RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /home/gradle/.gradle/gradle.properties

COPY . .

RUN chmod +x ./gradlew && ./gradlew clean build -x test

# 런타임 이미지
FROM openjdk:17.0-slim
WORKDIR /backend

COPY --from=build /home/gradle/project/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]