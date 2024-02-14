FROM openjdk:17.0-slim

WORKDIR /backend

# 빌드 과정에서 생성된 JAR 파일을 Docker 이미지 안의 작업 디렉토리로 복사
# 복사할 JAR 파일의 이름과 경로는 실제 프로젝트 구조와 빌드 설정에 따라 다를 수 있으므로 적절히 조정하세요.
COPY ./build/libs/*-SNAPSHOT.jar /backend/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]