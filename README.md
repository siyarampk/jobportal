# Job Portal

A full-stack **Job Portal** application consisting of a **Spring Boot 4 REST API backend** and a **React 19 single-page application (SPA) frontend**. The platform connects **job seekers**, **employers**, and **administrators** — job seekers can search jobs, manage their profile, and apply to jobs; employers can post and manage jobs and review applicants; admins manage companies, employers, and contact messages.

- **Backend** — Spring Boot (root of this repository)
- **Frontend** — React + Vite app in [`job-portal-ui/`](./job-portal-ui)

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Security Model](#-security-model)
- [API Versioning](#-api-versioning)
- [REST API Overview](#-rest-api-overview)
- [Database Schema](#-database-schema)
- [Observability & Monitoring](#-observability--monitoring)
- [Frontend Architecture](#-frontend-architecture)
- [Useful Commands](#-useful-commands)

---

## ✨ Features

### Job Seeker
- Browse and search jobs, view job details, and view company profiles
- Register and log in (JWT-based authentication)
- Create/update profile with **profile picture** and **resume** upload
- Save/unsave jobs and view saved jobs
- Apply to jobs with a cover letter, withdraw applications, and track application status

### Employer
- Post new jobs and manage own job listings (activate/close status)
- View applicants per job and update application status (review, shortlist, reject, etc.)
- View applications for the employer's company

### Admin
- Manage companies (create, update, delete)
- Search users by email, elevate users to the employer role, and assign companies to employers
- Manage contact messages (view, sort, paginate, update status)

### General
- Contact form for visitors
- Role-based access control (Admin / Employer / Job Seeker) on both backend and frontend
- Swagger UI for API documentation
- Caching (Caffeine), centralized logging with AOP, OpenTelemetry tracing/metrics/logs

---

## 🛠 Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| **Java 25** | Language/runtime |
| **Spring Boot 4.1.0** (Spring Framework 7) | Application framework |
| **Spring MVC** (`spring-boot-starter-webmvc`) | REST API |
| **Spring Data JPA + Hibernate** | Persistence (MySQL) |
| **MySQL** (`com.mysql:mysql-connector-j`) | Relational database |
| **Spring Security** | Authentication/authorization with **JWT** (`jjwt 0.13`) and **Cookie-based CSRF** |
| **Caffeine** | In-memory caching |
| **Bean Validation** | Request validation |
| **Spring Boot Actuator** | Health/metrics/management endpoints (separate port 9090) |
| **OpenTelemetry + Grafana LGTM** | Distributed tracing, metrics, logs |
| **SpringDoc OpenAPI 2.8** | Swagger UI / OpenAPI docs |

### Frontend (`job-portal-ui`)
| Technology | Purpose |
|---|---|
| **React 19** | UI library |
| **Vite 7** | Dev server & build tool |
| **Tailwind CSS 4** (`@tailwindcss/vite`) | Styling |
| **React Router 7** | Client-side routing |
| **Axios** | HTTP client with interceptors (JWT, CSRF, API versioning) |
| **js-cookie** | CSRF token cookie handling |
| **React Context API** | Auth, theme, and data state management |
| **Font Awesome + lucide-react** | Icons |
| **react-toastify** | Toast notifications |
| **ESLint 9** | Linting |

---

## 📁 Project Structure

```
jobportal/
├── pom.xml                          # Maven build (Spring Boot 4.1, Java 25)
├── mvnw / mvnw.cmd                  # Maven wrapper
├── compose.yml                      # Docker Compose: MySQL + Grafana otel-lgtm
├── logs/                            # Application logs
├── src/
│   └── main/
│       ├── java/com/eazybytes/jobportal/
│       │   ├── JobportalApplication.java      # Main Spring Boot class
│       │   ├── auth/                          # AuthController (login/register)
│       │   ├── user/                          # User controller & service
│       │   │   ├── controller/                #   profile, saved jobs, applications, admin
│       │   │   └── service/
│       │   ├── job/                           # Job controller & service (employer endpoints)
│       │   ├── company/                       # Company controller & service
│       │   ├── contact/                       # Contact form controller & service
│       │   ├── client/                        # Demo REST client (Todo/Post) integrations
│       │   ├── security/                      # Security config, JWT filter/util, CSRF, paths
│       │   │   ├── JobPortalSecurityConfig.java
│       │   │   ├── PathsConfig.java           # Public/secured/admin/employer/jobseeker paths
│       │   │   ├── filter/JwtTokenValidatorFilter.java
│       │   │   └── util/JwtUtil.java
│       │   ├── cache/CaffeineCacheConfig.java
│       │   ├── config/web/WebConfig.java      # API versioning + /api path prefix
│       │   ├── aspects/                       # AOP logging, performance, audit aspects
│       │   ├── audit/                         # JPA auditing (AuditorAware)
│       │   ├── otel/                          # OpenTelemetry log appender init
│       │   ├── entity/                        # JPA entities (User, Role, Company, Job,
│       │   │                                  #   JobApplication, Profile, Contact, BaseEntity)
│       │   ├── repository/                    # Spring Data repositories
│       │   ├── dto/                           # Request/response DTOs (records)
│       │   ├── exception/                     # Global exception handler
│       │   ├── constants/                     # Application constants
│       │   ├── scopes/                        # Demo request/session/application scoped beans
│       │   └── util/                          # Utilities
│       └── resources/
│           ├── application.properties         # Default config
│           ├── application-qa.properties      # QA profile
│           ├── application-prod.properties    # Prod profile
│           ├── jwt.properties                 # JWT issuer/expiration settings
│           ├── logback-spring.xml             # Logging configuration
│           └── sql/
│               ├── jobportal-schema.sql       # DDL: companies, jobs, users, roles, profiles,
│               │                              #   saved_jobs, job_applications, contacts
│               └── jobportal-data.sql         # Seed data (roles, companies, jobs, users)
└── job-portal-ui/                  # Frontend (React 19 + Vite 7)
    ├── package.json
    ├── vite.config.js              # React + Tailwind plugins
    ├── index.html
    ├── public/
    │   ├── logos/                  # Company logos (Amazon, Google, Netflix, ...)
    │   └── favicon.svg
    └── src/
        ├── main.jsx / App.jsx      # Entry point & routes
        ├── config/
        │   ├── api.js              # Centralized API base URL & endpoint definitions
        │   └── httpClient.js       # Axios instance: JWT, CSRF, Accept-version interceptors
        ├── context/                # AuthContext, ThemeContext, JobContext
        ├── contexts/               # JobsDataContext, CompaniesContext
        ├── services/               # companyService, jobApplicationService, savedJobService,
        │                           #   profileService, contactService
        ├── components/             # Layout, Navbar, Hero, JobsSection, CompaniesSection,
        │                           #   ApplyJobModal, ProtectedRoute, Footer, ...
        ├── pages/                  # Home, Jobs, JobDetail, Companies, CompanyDetail, Login,
        │   │                       #   Register, Profile, SavedJobs, AppliedJobs, Contact,
        │   │                       #   PostJob, MyJobs, JobApplicants
        │   └── admin/              # Dashboard, CompanyManagement, EmployerManagement,

---

## ✅ Prerequisites

| Tool | Version | Notes |
|---|---|---|
| **JDK** | 25+ | Required by `pom.xml` (`<java.version>25</java.version>`) |
| **Docker + Docker Compose** | Latest | For MySQL & Grafana LGTM (auto-started by Spring Boot docker-compose support) |
| **Node.js** | 18+ / 20+ (22 used in dev) | For the frontend |
| **npm** | 10+ | Bundled with Node.js |

Maven itself is not required — use the bundled wrapper (`./mvnw`).

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/siyarampk/jobportal.git
cd jobportal
```

### 2. Start the infrastructure (MySQL + Grafana)

The backend ships with **Spring Boot Docker Compose support** (`spring-boot-docker-compose`), so running the app automatically starts the containers defined in [`compose.yml`](./compose.yml):

- `jobportaldb` — **MySQL** on port **3306** (database `jobportal`, root password `root`)
- `grafana-lgtm` — **Grafana/Loki/Tempo/Prometheus** on ports **3000** (Grafana UI), **4317/4318** (OTLP)

You can also start them manually:

```bash
docker compose up -d
```

### 3. Run the backend

```bash
./mvnw spring-boot:run
```

- API base URL: **http://localhost:8080/api**
- Swagger UI: **http://localhost:8080/swagger-ui/index.html**
- Actuator: **http://localhost:9090/jobportal/actuator**
- Grafana dashboard: **http://localhost:3000**

> The database schema (`sql/jobportal-schema.sql`) and seed data (`sql/jobportal-data.sql`) are initialized automatically by Spring Boot SQL init.

### 4. Run the frontend

```bash
cd job-portal-ui
npm install
npm run dev
```

- App URL: **http://localhost:5173**
- The frontend talks to the backend at `http://localhost:8080/api` by default (see [Configuration](#-configuration)).

### 5. Sign in

Register a new account (registered users get the **JOB_SEEKER** role), or use seeded users from `sql/jobportal-data.sql` for admin/employer roles. CORS and security are pre-configured to allow `http://localhost:5173`.

---

## ⚙️ Configuration

### Environment variables (backend)

Database connection is resolved with env-var fallbacks (defaults in parentheses):

| Variable | Default | Description |
|---|---|---|
| `DATABASE_HOST` | `localhost` | MySQL host |
| `DATABASE_PORT` | `3306` | MySQL port |
| `DATABASE_NAME` | `jobportal` | Database name |
| `DATABASE_USERNAME` | `root` | DB username |
| `DATABASE_PASSWORD` | `root` | DB password |
| `LOG_LEVEL` | `INFO` | Application log level |
| `SHOW_SQL` | `true` | Hibernate SQL logging |
| `CONSOLE_LOG_PATTERN` | colored pattern | Console log format |

### Spring profiles

| Profile | File | Notes |

---

## 🔐 Security Model

The security configuration lives in [`JobPortalSecurityConfig`](./src/main/java/com/eazybytes/jobportal/security/JobPortalSecurityConfig.java) and [`PathsConfig`](./src/main/java/com/eazybytes/jobportal/security/PathsConfig.java):

- **Authentication** — `POST /api/auth/login/public` returns a **JWT** (24h validity in non-prod, 1h in prod). The JWT is stored in `localStorage` by the frontend and sent as `Authorization: Bearer <token>`.
- **Authorization** — Role-based path security:
  - **Public** — contact form, login, register, public company list, CSRF token, Swagger, actuator
  - **ROLE_JOB_SEEKER** — profile (view/update/picture/resume), saved jobs, job applications
  - **ROLE_EMPLOYER** — post/manage jobs, view & update applications
  - **ROLE_ADMIN** — contact management, company management, user/role management
  - Everything else → `denyAll()`
- **CSRF** — `CookieCsrfTokenRepository` (`XSRF-TOKEN` cookie, non-HttpOnly). The frontend automatically fetches a token from `/api/csrf-token/public` and sends `X-XSRF-TOKEN` on all non-safe methods (POST/PUT/PATCH/DELETE).
- **Passwords** — BCrypt encoding + HaveIBeenPwned compromised-password checker.
- **401/403** — JSON error responses; the frontend clears auth state and redirects to `/login` on 401.

### Roles

| Role | Description |
|---|---|
| `ROLE_JOB_SEEKER` | Default role assigned at registration |
| `ROLE_EMPLOYER` | Can post jobs and manage applicants (assigned by admin) |
| `ROLE_ADMIN` | Full administrative access |

---

## 🔢 API Versioning

The backend uses **Spring Framework 7 media-type-parameter API versioning** (configured in [`WebConfig`](./src/main/java/com/eazybytes/jobportal/config/web/WebConfig.java)):

- All REST endpoints are prefixed with **`/api`** (`addPathPrefix("/api", …)`).
- Clients send an `Accept` header of the form: `application/vnd.eazyapp+json;v=1.0`
- Supported versions: **1.0, 2.0, 3.0** (default: `1.0`)
- Controllers declare `version = "1.0"` on versioned endpoints.

The frontend's Axios interceptor sets this header automatically; `withApiVersion("2.0")` can be used for a specific version.

|---|---|---|
| *(default)* | `application.properties` | Local development; docker-compose auto-start; tracing enabled |
| `qa` | `application-qa.properties` | Longer cache TTLs, QA CORS origin |
| `prod` | `application-prod.properties` | SQL logging off, docker-compose off, tracing/OTLP export disabled, 1-hour JWT expiry (via `jwt.properties`) |


---

## 🌐 REST API Overview

Base URL: `http://localhost:8080/api` · Full docs: **Swagger UI** at `/swagger-ui/index.html`

### Auth (public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login/public` | Authenticate, returns JWT + user info |
| POST | `/auth/register/public` | Register (assigns JOB_SEEKER role) |
| GET | `/csrf-token/public` | Issue CSRF token cookie |

### Companies
| Method | Endpoint | Access |
|---|---|---|
| GET | `/companies/public` | Public |
| GET / POST | `/companies/admin` | Admin |
| PUT / DELETE | `/companies/{id}/admin` | Admin |

### Jobs
| Method | Endpoint | Access |
|---|---|---|
| GET / POST | `/jobs/employer` | Employer |
| PATCH | `/jobs/{jobId}/status/employer` | Employer |
| GET | `/jobs/applications/{jobId}/employer` | Employer |
| PATCH | `/jobs/applications/employer` | Employer |

### Users / Job Seeker
| Method | Endpoint | Access |
|---|---|---|
| GET / PUT | `/users/profile/jobseeker` | Job Seeker |
| GET | `/users/profile/picture/jobseeker` | Job Seeker (profile picture) |
| GET | `/users/profile/resume/jobseeker` | Job Seeker (resume) |
| POST / DELETE | `/users/saved-jobs/{jobId}/jobseeker` | Job Seeker (save/unsave) |
| GET | `/users/saved-jobs/jobseeker` | Job Seeker (saved jobs list) |
| POST | `/users/job-applications/jobseeker` | Job Seeker (apply) |
| DELETE | `/users/job-applications/{jobId}/jobseeker` | Job Seeker (withdraw) |
| GET | `/users/job-applications/jobseeker` | Job Seeker (my applications) |

### Users / Admin
| Method | Endpoint | Access |
|---|---|---|
| GET | `/users/search/admin` | Admin (find user by email) |
| PATCH | `/users/{userId}/role/employer/admin` | Admin (elevate to employer) |
| PATCH | `/users/{userId}/company/{companyId}/admin` | Admin (assign company) |

### Contacts
| Method | Endpoint | Access |
|---|---|---|
| POST | `/contacts/public` | Public (submit message) |
| GET | `/contacts` | Authenticated |
| GET | `/contacts/admin`, `/contacts/sort/admin`, `/contacts/page/admin` | Admin |
| PATCH | `/contacts/{id}/status/admin` | Admin |

---

## 🗄 Database Schema

MySQL schema is defined in [`src/main/resources/sql/jobportal-schema.sql`](./src/main/resources/sql/jobportal-schema.sql):

| Table | Purpose |
|---|---|
| `roles` | Role definitions (JOB_SEEKER, EMPLOYER, ADMIN) |
| `users` | User accounts (bcrypt password hash, FK to role & optional company) |
| `profiles` | Job-seeker profiles incl. profile picture & resume (MEDIUMBLOB) |
| `companies` | Company directory (logo, industry, size, rating, locations, …) |
| `jobs` | Job postings (salary range, work/job type, category, status ACTIVE/CLOSED/DRAFT) |
| `saved_jobs` | Job-seeker ↔ job bookmarks (composite PK) |
| `job_applications` | Applications with status workflow (PENDING → REVIEWED → SHORTLISTED → INTERVIEWED → OFFERED / REJECTED / WITHDRAWN), unique per (user, job) |
| `contacts` | Contact-form submissions with status |

All tables include audit columns (`created_at/by`, `updated_at/by`) maintained via JPA auditing.

---

## 📊 Observability & Monitoring

- **Actuator** — exposed on a separate management port **9090** at base path `/jobportal/actuator` (all endpoints exposed; health shows details).
- **OpenTelemetry** — traces, metrics, and logs are exported via OTLP to `http://localhost:4318/v1/{traces|metrics|logs}` (the Grafana **otel-lgtm** container). The `otel/OpenTelemetryAppenderInitializer` wires logback into OTEL.
- **Grafana UI** — http://localhost:3000
- **AOP aspects** — `LoggingAndPerformanceAspect`, `LogAspect`, `LoginSuccessAuditAspect`, `ExceptionAuditAspect`, and `RegisterValidationAspect` provide cross-cutting logging/auditing.
- **Logs** — written to `logs/jobportal.log` with rotation to `logs/archived/` (see `logback-spring.xml`).

---

## 💻 Frontend Architecture

- **Routing** (`App.jsx`) — public pages (Home, Jobs, Companies, Login, Register, Contact) plus role-protected routes wrapped in `ProtectedRoute`:
  - Job Seeker: `/profile`, `/applied-jobs`, `/saved-jobs`
  - Employer: `/post-job`, `/employer/jobs`, `/job-applicants/:jobId`
  - Admin: `/admin`, `/admin/companies`, `/admin/employers`, `/admin/contact-messages`
- **State** — React Contexts: `AuthContext` (JWT + user in `localStorage`), `ThemeContext` (dark/light), `JobsDataContext`, `CompaniesContext`, `JobContext`.
- **HTTP layer** — `config/httpClient.js` (Axios) adds:
  - `Accept: application/vnd.eazyapp+json;v=1.0` (API versioning)
  - `Authorization: Bearer <jwt>` for non-public endpoints
  - Automatic CSRF token fetch + `X-XSRF-TOKEN` header for mutating requests
  - 401 handling → clear session and redirect to `/login`
- **API endpoints** — centralized in `config/api.js`; feature logic in `src/services/*Service.js`.
- **Styling** — Tailwind CSS 4 with dark-mode support.

### Scripts

```bash
npm run dev       # Start dev server (http://localhost:5173)
npm run build     # Production build
npm run preview   # Preview production build
npm run lint      # ESLint
```

---

## 🧪 Testing & Build (Backend)

```bash
./mvnw test                     # Run tests
./mvnw clean package            # Build jar -> target/jobportal-aws-deployment.jar
./mvnw clean package -DskipTests
java -jar target/jobportal-aws-deployment.jar
```

---

## 📌 Useful Commands

| Command | Description |
|---|---|
| `docker compose up -d` | Start MySQL + Grafana LGTM |
| `docker compose stop` | Stop infrastructure |
| `./mvnw spring-boot:run` | Run backend (auto-starts docker compose) |
| `./mvnw spring-boot:run -Dspring-boot.run.profiles=qa` | Run with QA profile |
| `cd job-portal-ui && npm run dev` | Run frontend dev server |
| `./mvnw test` | Run backend tests |


```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### JWT settings (`jwt.properties`)

| Property | Value |
|---|---|
| `jwt.issuer` | `Eazy Job Portal` |
| `jwt.expiration.hours` | `24` (non-prod) |
| `jwt.prod.expiration.hours` | `1` (prod) |

### CORS

Configured via `app.cors.*` properties (`allowed-origins` includes `http://localhost:5173` by default). The frontend Vite dev server must run on an allowed origin.

### Frontend environment

Create `job-portal-ui/.env` to override the backend URL:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

        │                           #   ContactMessages
        └── data/mockData.js        # Fallback/mock data for offline development
```

| **REST Client** (`spring-boot-starter-restclient`) | Outbound HTTP calls (demo Todo/Post integrations) |
| **Spring AOP** | Logging, performance, and audit aspects |
| **Lombok** | Boilerplate reduction |
| **Docker Compose support** | Auto-start MySQL + Grafana LGTM containers on app startup |
