# Java 17 기반 이미지 (Eclipse Temurin)
FROM eclipse-temurin:17-jdk-jammy

# 작업 디렉토리
WORKDIR /app

# Gradle Wrapper 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test

# JAR 파일 실행
EXPOSE 8080
CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]
