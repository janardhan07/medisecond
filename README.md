# MediSecond — Second Opinion Medical App
### Java 21 (Spring Boot) + React (Vite)

---

## What This App Does

MediSecond lets patients submit medical cases, get an AI-powered specialty suggestion,
chat with their assigned doctor, and pay for a consultation — all in one place.

**Roles:**
- **PATIENT** — Submit cases, chat with doctor, pay for consultation
- **DOCTOR** — View assigned cases, chat with patient
- **ADMIN** — View all cases

---

## Prerequisites

Install these once and you are set for life:

| Tool | Download |
|------|----------|
| Java 21 JDK | https://adoptium.net (pick Java 21) |
| Maven | https://maven.apache.org/download.cgi OR bundled with most IDEs |
| Node.js 18+ | https://nodejs.org (LTS version) |

> **Check if installed:**
> ```bash
> java -version      # should say 21
> mvn -version       # any version is fine
> node -version      # should say 18+
> npm -version       # comes with Node
> ```

---

## Run the Backend (Spring Boot / Java)

```bash
# 1. Go into the backend folder
cd medisecond/backend

# 2. Start the server (downloads dependencies automatically on first run)
mvn spring-boot:run
```

✅ You will see: `Started MedisecondApplication in X seconds`

The backend runs at: **http://localhost:8080**

> **H2 Database Console** (inspect data in browser):
> http://localhost:8080/h2-console
> JDBC URL: `jdbc:h2:mem:mediseconddb`   User: `sa`   Password: *(leave blank)*

---

## Run the Frontend (React / Vite)

Open a **new terminal** (keep backend running):

```bash
# 1. Go into the frontend folder
cd medisecond/frontend

# 2. Install dependencies (only needed once)
npm install

# 3. Start the dev server
npm run dev
```

✅ You will see: `Local: http://localhost:5173`

Open **http://localhost:5173** in your browser.

---

## First Time Use

1. **Register** — Click "Sign Up", choose Patient or Doctor, fill in details
2. **Login** — Use your credentials
3. **Patient flow:**
   - Click **+ New Case** → fill title, symptoms, description → submit
   - The AI instantly suggests the right medical specialty
   - Click **💬 Chat** to message the doctor
   - Click **💳 Pay Now** to complete the consultation payment
4. **Doctor flow:**
   - Login as a Doctor account to see assigned cases and chat

---

## Project Structure

```
medisecond/
├── backend/                          ← Spring Boot (Java 21)
│   ├── pom.xml                       ← Dependencies (Maven)
│   └── src/main/java/com/medisecond/
│       ├── MedisecondApplication.java   ← Entry point
│       ├── config/
│       │   ├── SecurityConfig.java      ← JWT + CORS security
│       │   └── JwtAuthFilter.java       ← Token validation on every request
│       ├── controller/
│       │   ├── AuthController.java      ← /api/auth/*
│       │   ├── MedicalCaseController.java  ← /api/medical/cases
│       │   ├── ChatController.java      ← /api/appointments/cases/{id}/chat
│       │   └── PaymentController.java   ← /api/billing/*
│       ├── service/
│       │   ├── JwtService.java          ← Token creation & validation
│       │   ├── UserService.java         ← User registration & lookup
│       │   ├── MlService.java           ← AI specialty prediction
│       │   ├── MedicalCaseService.java
│       │   ├── ChatService.java
│       │   └── PaymentService.java      ← Simulated Razorpay
│       ├── model/                       ← JPA Entities (database tables)
│       ├── repository/                  ← Spring Data JPA queries
│       └── dto/                         ← Request/Response objects
│
└── frontend/                         ← React (Vite)
    ├── package.json
    ├── vite.config.js                ← Proxies /api → :8080
    └── src/
        ├── App.jsx                   ← Routes
        ├── context/AuthContext.jsx   ← Login state + localStorage
        ├── services/api.js           ← Axios with JWT interceptor
        └── pages/
            ├── Login.jsx
            ├── Register.jsx
            ├── Dashboard.jsx
            ├── NewCase.jsx
            ├── Chat.jsx
            └── Payment.jsx
```

---

## API Endpoints Reference

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/auth/register` | No | Create account |
| POST | `/api/auth/login` | No | Get JWT token |
| GET | `/api/auth/profile` | Yes | Get current user |
| GET | `/api/medical/cases` | Yes | List cases (role-filtered) |
| POST | `/api/medical/cases` | Yes | Create a case (Patient only) |
| GET | `/api/medical/cases/{id}` | Yes | Get one case |
| GET | `/api/appointments/cases/{id}/chat` | Yes | Get messages |
| POST | `/api/appointments/cases/{id}/chat` | Yes | Send message |
| POST | `/api/billing/create_order` | Yes | Create payment order |
| POST | `/api/billing/verify_payment` | Yes | Verify & confirm payment |

---

## Common Issues

| Problem | Fix |
|---------|-----|
| `java: error: release version 21 not supported` | Install Java 21 from adoptium.net |
| `Port 8080 already in use` | Stop other apps on 8080, or change `server.port` in `application.properties` |
| `npm: command not found` | Install Node.js from nodejs.org |
| Frontend shows blank page | Make sure backend is running on port 8080 first |
| Login returns 401 | Register a new account first — database resets on each backend restart |

---

## Production Notes (when you're ready)

- Replace H2 with PostgreSQL: add `spring-boot-starter-postgres` to pom.xml, update `application.properties`
- Replace simulated Razorpay with the real Java SDK: `com.razorpay:razorpay-java`
- Change `SECRET_KEY` in `application.properties` to a strong random value
- Set `DEBUG=false` equivalent: `logging.level.root=WARN`
