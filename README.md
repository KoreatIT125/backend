# Fish Detection Backend

방어, 부시리, 잿방어 분류 AI 서비스 백엔드 API

## 🚀 Tech Stack

- Python 3.10+
- FastAPI / Flask
- Ultralytics YOLO
- PostgreSQL / MySQL
- Docker

## 📁 Project Structure

```
backend/
├── app/
│   ├── api/            # API 엔드포인트
│   │   ├── routes/
│   │   └── deps.py
│   ├── core/           # 설정, 보안
│   │   ├── config.py
│   │   └── security.py
│   ├── models/         # DB 모델
│   ├── schemas/        # Pydantic 스키마
│   ├── services/       # 비즈니스 로직
│   │   └── prediction.py
│   └── main.py
├── tests/
├── requirements.txt
├── Dockerfile
└── README.md
```

## 🛠️ Setup

```bash
# 가상환경 생성
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 의존성 설치
pip install -r requirements.txt

# 서버 실행
uvicorn app.main:app --reload
```

## 🔗 API Endpoints

- `POST /api/v1/predict` - 이미지 업로드 및 예측
- `GET /api/v1/history` - 예측 히스토리
- `GET /api/v1/stats` - 통계

## 🐳 Docker

```bash
docker build -t fish-detection-backend .
docker run -p 8000:8000 fish-detection-backend
```

## 👥 Team

Backend Team
