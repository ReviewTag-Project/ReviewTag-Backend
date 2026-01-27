# 1. 빌드 단계 (Java 21)
FROM amazoncorretto:21 AS builder
WORKDIR /app
COPY . .

# Maven이 압축 풀 때 필요한 도구 설치
RUN yum install -y tar gzip

# 실행 권한 부여 및 빌드 (Maven 사용)
# ★ 중요: 폴더 안에 'mvnw' 파일이 꼭 있어야 합니다!
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# 2. 실행 단계 (Java 21)
FROM amazoncorretto:21
WORKDIR /app

# war 파일 복사 (Maven은 target 폴더에 생김)
COPY --from=builder /app/target/*.war app.war

# 실행
ENTRYPOINT ["java", "-jar", "app.war"]
