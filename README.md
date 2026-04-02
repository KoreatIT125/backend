# 🐟 Backend - 북태평양 연어 지능형 양식장

Spring Boot 기반 REST API 서버 및 데이터베이스 관리

---

## 📋 **목차**

- [개요](#-개요)
- [주요 기능](#-주요-기능)
- [API 명세](#-api-명세)
- [데이터베이스 스키마](#-데이터베이스-스키마)
- [설치 및 실행](#-설치-및-실행)
- [디렉토리 구조](#-디렉토리-구조)

---

## 🎯 **개요**

본 Backend 서버는 **Spring Boot 2.7.18**과 **Java 17**을 사용하여 구축되었습니다.

### **주요 역할**
- REST API 제공 (Frontend ↔ Backend)
- AI 모델 서버 연동 (Backend ↔ AI Model)
- 센서 데이터 수집 및 저장
- 사료 공급 관리
- 수질 모니터링
- 이력 데이터 관리

---

## ✨ **주요 기능**

### **1. 연어 감지 및 관리**
- AI 모델 호출 (개체 감지, 크기 측정)
- 감지 이력 저장
- 통계 조회 (일별/주별/월별)

### **2. 수질 센서 데이터 관리**
- 실시간 센서 데이터 수집 (수온, 염도, DO, pH, ORP, 조도)
- 시계열 데이터 저장
- 이상 수질 경보

### **3. 사료 공급 관리**
- 사료 공급 이력 기록
- 최적 급이량 계산
- 급이 스케줄 관리

### **4. 수조 관리**
- 수조 정보 관리
- 개체 수 추적
- 성장률 분석

---

## 📡 **API 명세**

### **Base URL**: `http://localhost:8080/api`

### **1. Health Check**
```http
GET /health
```

**Response**:
```json
{
  "status": "UP",
  "database": "connected",
  "aiModel": "available",
  "timestamp": "2026-04-02T18:45:00+09:00"
}
```

---

### **2. 연어 감지 API**

#### **이미지 분석 요청**
```http
POST /fish/analyze
Content-Type: multipart/form-data
```

**Request**:
```
image: File
tank_id: String
```

**Response**:
```json
{
  "success": true,
  "tank_id": "tank_001",
  "count": 487,
  "average_size": {
    "length_mm": 223,
    "weight_g": 115
  },
  "timestamp": "2026-04-02T18:45:00+09:00"
}
```

#### **감지 이력 조회**
```http
GET /fish/history?tank_id=tank_001&days=7
```

**Response**:
```json
{
  "tank_id": "tank_001",
  "period": "2026-03-26 ~ 2026-04-02",
  "records": [
    {
      "date": "2026-04-02",
      "count": 487,
      "average_length_mm": 223,
      "average_weight_g": 115
    }
  ]
}
```

---

### **3. 수질 센서 API**

#### **센서 데이터 조회**
```http
GET /sensor/water?tank_id=tank_001&hours=24
```

**Response**:
```json
{
  "tank_id": "tank_001",
  "latest": {
    "temperature_c": 18.8,
    "salinity_ppt": 31.9,
    "do_mg_l": 7.2,
    "ph": 7.3,
    "orp_mv": 325.1,
    "illuminance_lux": 89.1,
    "measured_at": "2026-04-02T18:44:00+09:00"
  },
  "history": [...]
}
```

#### **센서 데이터 저장 (IoT 장치에서 호출)**
```http
POST /sensor/water
Content-Type: application/json
```

**Request**:
```json
{
  "tank_id": "tank_001",
  "temperature_c": 18.8,
  "salinity_ppt": 31.9,
  "do_mg_l": 7.2,
  "ph": 7.3,
  "orp_mv": 325.1,
  "illuminance_lux": 89.1
}
```

---

### **4. 사료 공급 API**

#### **사료 공급 기록**
```http
POST /feed/supply
Content-Type: application/json
```

**Request**:
```json
{
  "tank_id": "tank_001",
  "feed_type": "salmon_feed",
  "amount_g": 1340,
  "protein_percent": 45,
  "fat_percent": 12
}
```

#### **급이 이력 조회**
```http
GET /feed/history?tank_id=tank_001&days=7
```

---

### **5. 수조 관리 API**

#### **수조 목록 조회**
```http
GET /tank/list
```

**Response**:
```json
{
  "tanks": [
    {
      "tank_id": "tank_001",
      "name": "1번 수조",
      "type": "유수식",
      "radius_cm": 520,
      "height_cm": 120,
      "current_count": 487,
      "status": "normal"
    }
  ]
}
```

---

## 🗄️ **데이터베이스 스키마**

### **1. fish_detection (연어 감지 이력)**
```sql
CREATE TABLE fish_detection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tank_id VARCHAR(50) NOT NULL,
    count INT NOT NULL,
    average_length_mm DECIMAL(6,2),
    average_weight_g DECIMAL(7,2),
    image_path VARCHAR(255),
    detected_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### **2. water_sensor (수질 센서 데이터)**
```sql
CREATE TABLE water_sensor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tank_id VARCHAR(50) NOT NULL,
    temperature_c DECIMAL(4,2),
    salinity_ppt DECIMAL(4,2),
    do_mg_l DECIMAL(4,2),
    ph DECIMAL(3,2),
    orp_mv DECIMAL(5,2),
    illuminance_lux DECIMAL(6,2),
    measured_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tank_measured (tank_id, measured_at)
);
```

### **3. feed_supply (사료 공급 이력)**
```sql
CREATE TABLE feed_supply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tank_id VARCHAR(50) NOT NULL,
    feed_type VARCHAR(50) NOT NULL,
    amount_g DECIMAL(7,2) NOT NULL,
    protein_percent DECIMAL(4,2),
    fat_percent DECIMAL(4,2),
    supplied_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### **4. tank_info (수조 정보)**
```sql
CREATE TABLE tank_info (
    tank_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    radius_cm INT,
    height_cm INT,
    location VARCHAR(255),
    status VARCHAR(20) DEFAULT 'active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 🚀 **설치 및 실행**

### **1. 사전 요구사항**
- Java 17 (LTS)
- MySQL 8.0+
- Gradle 8.x

### **2. 데이터베이스 설정**
```sql
CREATE DATABASE salmon_farm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'salmon_user'@'localhost' IDENTIFIED BY 'salmon_pass123';
GRANT ALL PRIVILEGES ON salmon_farm.* TO 'salmon_user'@'localhost';
FLUSH PRIVILEGES;
```

### **3. 애플리케이션 설정**
`src/main/resources/application.yml` 수정:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/salmon_farm
    username: salmon_user
    password: salmon_pass123
```

### **4. 빌드 및 실행**
```bash
# Gradle 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

**접속**: http://localhost:8080

---

## 📁 **디렉토리 구조**

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/salmon/farm/
│   │   │   ├── SalmonFarmApplication.java      # 메인 클래스
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java              # CORS 설정
│   │   │   ├── controller/
│   │   │   │   ├── HealthController.java       # Health Check
│   │   │   │   ├── FishController.java         # 연어 감지 API
│   │   │   │   ├── SensorController.java       # 센서 API
│   │   │   │   ├── FeedController.java         # 사료 API
│   │   │   │   └── TankController.java         # 수조 API
│   │   │   ├── service/
│   │   │   │   ├── FishDetectionService.java   # 감지 비즈니스 로직
│   │   │   │   ├── SensorService.java          # 센서 데이터 처리
│   │   │   │   ├── FeedService.java            # 사료 관리
│   │   │   │   └── AiModelService.java         # AI 모델 호출
│   │   │   ├── repository/
│   │   │   │   ├── FishDetectionRepository.java
│   │   │   │   ├── WaterSensorRepository.java
│   │   │   │   ├── FeedSupplyRepository.java
│   │   │   │   └── TankInfoRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── FishDetection.java
│   │   │   │   ├── WaterSensor.java
│   │   │   │   ├── FeedSupply.java
│   │   │   │   └── TankInfo.java
│   │   │   └── dto/
│   │   │       ├── FishAnalysisRequest.java
│   │   │       ├── FishAnalysisResponse.java
│   │   │       └── SensorDataRequest.java
│   │   └── resources/
│   │       ├── application.yml                  # 설정 파일
│   │       └── data.sql                         # 초기 데이터
│   └── test/
│       └── java/com/salmon/farm/
│           └── SalmonFarmApplicationTests.java
├── build.gradle                                 # Gradle 설정
├── Dockerfile                                   # Docker 이미지
├── Jenkinsfile                                  # CI/CD
└── README.md                                    # 본 문서
```

---

## 🔧 **설정 파일**

### **application.yml**
```yaml
spring:
  application:
    name: salmon-farm-backend
  
  datasource:
    url: jdbc:mysql://localhost:3306/salmon_farm?useUnicode=true&characterEncoding=utf8mb4
    username: salmon_user
    password: salmon_pass123
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

# AI Model 서버 설정
ai-model:
  base-url: http://localhost:5000
  timeout-seconds: 30

server:
  port: 8080
```

---

## 🐳 **Docker 실행**

```bash
# 이미지 빌드
docker build -t salmon-backend:latest .

# 컨테이너 실행
docker run -d \
  --name salmon-backend \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/salmon_farm \
  salmon-backend:latest
```

---

## 🧪 **테스트**

```bash
# 단위 테스트
./gradlew test

# API 테스트 (Postman Collection 제공)
```

---

## 📝 **라이선스**

MIT License - [LICENSE](../LICENSE) 참조

---

**🐟 Spring Boot로 연어 양식장을 관리합니다!**
