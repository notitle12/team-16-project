# Dockerfile

# 사용할 베이스 이미지를 설정합니다.
FROM openjdk:17-jdk-slim

# 작업 디렉토리를 생성하고 설정합니다.
WORKDIR /app

# 프로젝트에서 생성된 JAR 파일을 이미지에 복사합니다.
COPY build/libs/order-management-0.0.1-SNAPSHOT.jar /app/order-management-0.0.1-SNAPSHOT.jar

# 컨테이너가 시작될 때 실행할 명령어를 설정합니다.
CMD ["java", "-jar", "/app/order-management-0.0.1-SNAPSHOT.jar"]
