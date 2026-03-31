# Backend - Disaster Safety System

재난 안전 시스템 백엔드 API 서버

## 🚀 Tech Stack

- **Java 17** (LTS)
- **Spring Boot 2.7.18** (Lombok 호환)
- **Gradle 7.6.4**
- **MySQL 8.0**
- **JPA/Hibernate**

## 📦 Development

### Prerequisites

- JDK 17
- MySQL 8.0

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

→ http://localhost:8080

### API Docs

→ http://localhost:8080/swagger-ui.html

## 🐳 Docker

```bash
docker build -t safety-backend .
docker run -p 8080:8080 safety-backend
```

## 📁 Project Structure

```
src/main/java/com/disaster/safety/
├── SafetyApplication.java
├── controller/
│   ├── HealthController.java
│   └── DetectionController.java
├── service/
│   └── DetectionService.java
├── dto/
│   ├── DetectionRequest.java
│   └── DetectionResponse.java
├── config/
│   └── WebConfig.java
└── repository/
    └── (Add your repositories here)
```

## 🔗 AI Model Integration

AI Model Server: `http://ai-model:5000`

```java
// DetectionService에서 AI 모델 호출
POST http://ai-model:5000/predict
- image: MultipartFile
- cctvType: String
```

## 📊 API Endpoints

### Health Check

```bash
GET /api/health
```

### Detection

```bash
POST /api/detection/analyze
- image: file
- cctvType: electrical_room | construction_site | general
```

### History

```bash
GET /api/detection/history
```

## 🗄️ Database

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/safety_db
    username: root
    password: password
```

## 🧪 Testing

```bash
./gradlew test
```

## 📝 License

MIT
