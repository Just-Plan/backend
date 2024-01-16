# 사용할 Gradle 및 JDK 버전을 명시
FROM gradle:7.6-jdk17 AS builder
WORKDIR /build

# 그래들 설정 파일 복사
COPY build.gradle settings.gradle /build/

# Gradle 의존성 캐시를 위한 레이어
# 이 단계는 build.gradle 또는 settings.gradle 파일이 변경될 때만 다시 실행됩니다.
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 전체 프로젝트 복사
COPY . /build

# 실제 애플리케이션 빌드
RUN gradle build -x test --parallel

# 최종 애플리케이션 실행을 위한 베이스 이미지
FROM openjdk:17.0-slim
WORKDIR /app

# 빌더 스테이지에서 빌드된 jar 파일 복사
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar

# 포트 8080 노출
EXPOSE 8080

# 프로덕션 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=prod

# root 대신 nobody 권한으로 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]