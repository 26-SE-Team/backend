# 1단계: 빌드 환경 (Gradle 빌드 도구 사용)
FROM gradle:8-jdk17 AS build
WORKDIR /app

# 빌드 캐시를 위해 Gradle 파일 먼저 복사
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

# 실행 권한 부여
RUN chmod +x gradlew

# 소스 코드 복사 및 실행 가능한 JAR 빌드 (테스트는 스킵하여 빌드 속도 향상)
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# 2단계: 실행 환경 (가벼운 JRE 실행 전용 이미지 사용)
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 백엔드 API 포트 개방
EXPOSE 8080

# 백엔드 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
