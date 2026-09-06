# 🎓 PlaceHub — College Placement Management Portal

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring Boot-3.2.0-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/Spring Security-6-darkgreen?style=for-the-badge&logo=springsecurity" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.1-green?style=for-the-badge&logo=thymeleaf" />
</p>

<p align="center">
  A full-stack web application that streamlines the entire college placement process —
  from student registration and job applications to company management and admin oversight.
</p>

<p align="center">
  🔗 <strong>Live Demo:</strong> <a href="https://placementportalsystem-production.up.railway.app/">https://placementportalsystem-production.up.railway.app/</a>
</p>

---

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [Getting Started](#-getting-started)
- [Demo Credentials](#-demo-credentials)
- [Screenshots](#-screenshots)
- [API & URL Routes](#-url-routes)
- [Authors](#-authors)

---

## 🚀 About the Project

**PlaceHub** is a College Placement Management Portal built using **Spring Boot**. It connects three types of users — **Students**, **Companies**, and **Admins** — on a single platform to manage the complete placement lifecycle.

- Students can browse eligible jobs and apply with one click
- Companies can post jobs and manage applicants through a multi-stage pipeline
- Admins can oversee all students, companies, jobs, and placement records

---

## ✨ Features

### 👨‍🎓 Student
- Register with complete academic profile (CGPA, backlogs, department, batch)
- Browse jobs automatically filtered by eligibility (CGPA & backlogs)
- Apply for jobs with one click
- Track application status through 8 stages in real time
- Update profile — phone, CGPA, resume link

### 🏢 Company
- Register company profile and await admin verification
- Post job openings with eligibility criteria (min CGPA, max backlogs, eligible departments)
- View all applicants for each job posting
- Update application status with remarks
- Auto-mark student as placed when status is set to SELECTED

### 🛡️ Admin
- Live dashboard with placement statistics and placement rate
- View and filter all registered students by department
- Approve or reject company registrations
- View all job postings across all companies
- View complete placement records with package details

### 🔐 Security
- Spring Security 6 with form-based login
- BCrypt password hashing
- Role-based access control (ADMIN, STUDENT, COMPANY)
- Each role redirected to its own dashboard after login
- CSRF disabled for simplicity

---

## 💻 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.0 |
| Web | Spring MVC |
| Security | Spring Security 6 + BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Frontend | Thymeleaf (server-side rendering) |
| Build | Apache Maven |
| Dev Tools | Spring Boot DevTools |

---

## 📁 Project Structure

```
placehub/
├── pom.xml
└── src/
    └── main/
        ├── java/com/placement/portal/
        │   ├── PlaceHubApplication.java          ← Main entry point
        │   ├── config/
        │   │   ├── SecurityConfig.java           ← Spring Security setup
        │   │   └── DataInitializer.java          ← Seeds demo data on startup
        │   ├── controller/
        │   │   ├── AuthController.java           ← Login & Registration
        │   │   ├── HomeController.java           ← Public home page
        │   │   ├── StudentController.java        ← Student dashboard & actions
        │   │   ├── CompanyController.java        ← Company dashboard & actions
        │   │   └── AdminController.java          ← Admin panel
        │   ├── model/
        │   │   ├── User.java                     ← UserDetails implementation
        │   │   ├── Student.java                  ← Student entity
        │   │   ├── Company.java                  ← Company entity
        │   │   ├── JobPosting.java               ← Job posting entity
        │   │   └── Application.java              ← Application entity
        │   ├── repository/
        │   │   ├── UserRepository.java
        │   │   ├── StudentRepository.java        ← Custom JPQL queries
        │   │   ├── CompanyRepository.java
        │   │   ├── JobPostingRepository.java
        │   │   └── ApplicationRepository.java
        │   ├── service/
        │   │   ├── StudentService.java
        │   │   ├── CompanyService.java
        │   │   └── CustomUserDetailsService.java
        │   └── exception/
        │       └── GlobalExceptionHandler.java
        └── resources/
            ├── application.properties
            └── templates/
                ├── home.html
                ├── error.html
                ├── auth/
                │   ├── login.html
                │   └── register.html
                ├── admin/
                │   ├── dashboard.html
                │   ├── students.html
                │   ├── companies.html
                │   ├── jobs.html
                │   └── placements.html
                ├── student/
                │   ├── dashboard.html
                │   ├── jobs.html
                │   ├── applications.html
                │   └── profile.html
                └── company/
                    ├── dashboard.html
                    ├── jobs.html
                    ├── new-job.html
                    ├── applications.html
                    └── profile.html
```

---

## 🗄️ Database Schema

```
users
├── id (PK)
├── name
├── email (UNIQUE)
├── password (BCrypt hashed)
├── role (ADMIN | STUDENT | COMPANY)
└── enabled

students
├── id (PK)
├── user_id (FK → users) [OneToOne]
├── roll_number (UNIQUE)
├── department
├── batch
├── cgpa
├── phone
├── backlogs
├── tenth_percentage
├── twelfth_percentage
├── resume_path
├── placement_status (NOT_PLACED | PLACED | OPTED_OUT)
├── placed_company
└── placement_package

student_skills
├── student_id (FK → students)
└── skill

companies
├── id (PK)
├── user_id (FK → users) [OneToOne]
├── company_name
├── industry
├── website
├── description
├── location
├── contact_person
├── phone
└── verification_status (PENDING | VERIFIED | REJECTED)

job_postings
├── id (PK)
├── company_id (FK → companies) [ManyToOne]
├── title
├── description
├── location
├── job_type (FULL_TIME | INTERNSHIP | PART_TIME | CONTRACT)
├── package_lpa
├── min_cgpa
├── max_backlogs
├── eligible_departments
├── application_deadline
├── drive_date
├── status (ACTIVE | CLOSED | DRAFT)
└── created_at

applications
├── id (PK)
├── student_id (FK → students) [ManyToOne]
├── job_posting_id (FK → job_postings) [ManyToOne]
├── status (APPLIED | SHORTLISTED | APTITUDE_TEST | TECHNICAL_INTERVIEW | HR_INTERVIEW | SELECTED | REJECTED | ON_HOLD)
├── applied_at
└── remarks
```

### Entity Relationships

```
User (1) ──── (1) Student
User (1) ──── (1) Company
Company (1) ──── (Many) JobPosting
Student (1) ──── (Many) Application
JobPosting (1) ──── (Many) Application
```

---

## 🛠️ Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0
- Any IDE (IntelliJ IDEA / STS / Eclipse)

### Step 1 — Clone the Repository

```bash
git clone https://github.com/yourusername/placehub.git
cd placehub
```

### Step 2 — Create MySQL Database

Open MySQL and run:

```sql
CREATE DATABASE placementdb;
```

### Step 3 — Configure application.properties

Open `src/main/resources/application.properties` and update your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placementdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### Step 4 — Run the Application

```bash
mvn spring-boot:run
```

Or run `PlaceHubApplication.java` directly from your IDE.

### Step 5 — Open in Browser

```
http://localhost:8080
```

Hibernate will automatically create all tables and seed demo data on first startup.

---

## 🔑 Demo Credentials

| Role | Email | Password |
|---|---|---|
| 🛡️ Admin | admin@placehub.com | admin123 |
| 👨‍🎓 Student | rahul@student.com | student123 |
| 👩‍🎓 Student | priya@student.com | student123 |
| 👨‍🎓 Student | amit@student.com | student123 |
| 🏢 Company | hr@techcorp.com | company123 |
| 🏢 Company | hr@innovatetech.com | company123 |

---

## 🌐 URL Routes

### Public
| URL | Description |
|---|---|
| `GET /` | Home page with active jobs |
| `GET /auth/login` | Login page |
| `GET /auth/register` | Registration page |
| `POST /auth/register/student` | Student registration |
| `POST /auth/register/company` | Company registration |

### Admin (`/admin/**`)
| URL | Description |
|---|---|
| `GET /admin/dashboard` | Admin dashboard with stats |
| `GET /admin/students` | View all students |
| `GET /admin/companies` | View and verify companies |
| `GET /admin/jobs` | View all job postings |
| `GET /admin/placements` | View placement records |
| `POST /admin/companies/{id}/verify` | Approve or reject company |

### Student (`/student/**`)
| URL | Description |
|---|---|
| `GET /student/dashboard` | Student dashboard |
| `GET /student/jobs` | Browse eligible jobs |
| `POST /student/apply/{jobId}` | Apply for a job |
| `GET /student/applications` | View my applications |
| `GET /student/profile` | View profile |
| `POST /student/profile/update` | Update profile |

### Company (`/company/**`)
| URL | Description |
|---|---|
| `GET /company/dashboard` | Company dashboard |
| `GET /company/jobs` | View my job postings |
| `GET /company/jobs/new` | New job form |
| `POST /company/jobs/new` | Post a new job |
| `GET /company/jobs/{id}/applications` | View applicants |
| `POST /company/applications/{id}/status` | Update application status |
| `GET /company/profile` | Company profile |

---

## 📊 Application Status Flow

```
APPLIED → SHORTLISTED → APTITUDE_TEST → TECHNICAL_INTERVIEW → HR_INTERVIEW → SELECTED
                                                                            ↘ REJECTED
                                                                            ↘ ON_HOLD
```

---

## 🔧 Key Implementation Details

### Job Eligibility Filtering (JPQL)
Jobs are automatically filtered for each student based on their CGPA and backlogs:
```java
@Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' " +
       "AND j.minCgpa <= :cgpa AND j.maxBacklogs >= :backlogs " +
       "AND j.applicationDeadline >= :today")
List<JobPosting> findEligibleJobs(Double cgpa, Integer backlogs, LocalDate today);
```

### Lazy Loading Fix (JOIN FETCH)
To avoid `LazyInitializationException`, all queries use `JOIN FETCH`:
```java
@Query("SELECT s FROM Student s JOIN FETCH s.user")
List<Student> findAllWithUser();
```

### Auto Data Seeding
Demo data is seeded on application startup using `CommandLineRunner`:
```java
@Component
public class DataInitializer implements CommandLineRunner {
    public void run(String... args) {
        if (userRepo.count() > 0) return; // skip if data exists
        // seed admin, students, companies, jobs...
    }
}
```

---

## 👤 Authors

- **Niyati Patel**
- **Stuti Pandya**
- **Dharmesh Prajapati**

---

<p align="center">
  Built with ❤️ using Spring Boot | Java | MySQL
</p>
