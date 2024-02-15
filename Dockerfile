# gradle:7.3.1-jdk17 이미지를 기반으로 함
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 프록시 설정을 gradle.properties에 추가
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /home/gradle/.gradle/gradle.properties

# Spring 소스 코드를 이미지에 복사
COPY . .

# gradlew 실행 권한 부여 및 프로젝트 빌드
RUN chmod +x ./gradlew && ./gradlew clean build -x test

# 런타임 이미지
FROM openjdk:17.0-slim
WORKDIR /app

# 빌드 결과 jar 파일을 실행
COPY --from=build /home/gradle/project/build/libs/*-SNAPSHOT.jar ./app.jar

# DATABASE_URL을 환경 변수로 삽입
ENV DATABASE_URL=jdbc:mariadb://mariadb/krampoline

EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
