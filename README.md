# JobFlow – AI Powered Resume Intelligence Platform

JobFlow is a full-stack AI-powered resume analysis platform that helps users optimize resumes for specific job descriptions, improve ATS compatibility, and prepare for interviews through personalized AI-driven insights.

Built using **React + Spring Boot + PostgreSQL + Llama 3.3 (Groq API)**.

---

## Features

### Resume Analysis
- Upload resume PDFs
- Extracts structured information automatically:
  - Name
  - Skills
  - Experience
  - Projects
  - Education
  - Certifications
- Generates AI-powered feedback

---

### JD-Aware ATS Scoring Engine

Unlike generic resume scoring systems, JobFlow calculates scores relative to a target Job Description.

Scoring dimensions:

| Dimension | Weight |
|------------|---------|
| Skills Match | 20 |
| Resume Structure | 20 |
| Experience Quality | 20 |
| Project Quality | 20 |
| Keyword Optimization | 20 |

ATS Score Calculation:

```text
Resume
↓
Resume Parsing
↓
Job Description Parsing
↓
Skill Matching
↓
Keyword Overlap Detection
↓
Experience Evaluation
↓
Project Relevance Evaluation
↓
Final ATS Score
```

---

### AI Resume Insights

Generates:

- ATS Score Explanation
- Resume Strengths
- Areas for Improvement
- ATS Optimization Suggestions
- Better Bullet Point Suggestions
- Skills Gap Analysis
- Personalized Interview Questions

---

### Personalized Interview Practice

Questions are dynamically generated based on:

- Resume content
- Projects
- Skills
- Experience
- Target Job Description

Users can:

- Answer interview questions
- Receive AI evaluation
- Get score out of 10
- Receive personalized improvement feedback

---

### Resume Comparison Dashboard

Compare multiple resume versions visually.

Comparison dimensions:

- Skills Match
- Keywords
- Experience
- Projects
- Resume Structure
- Overall ATS Score

Features:

- Radar chart visualization
- Score comparison table
- Resume progression tracking

---

### Resume History

Stores previous resume analyses:

- Resume filename
- ATS score
- Analysis date
- AI feedback
- Interview questions

Users can revisit previous analyses instantly.

---

##  Tech Stack

### Frontend
- React
- JSX
- CSS
- Vite

### Backend
- Java
- Spring Boot
- REST APIs

### Database
- PostgreSQL

### AI
- Llama 3.3 70B Versatile
- Groq API

### Authentication
- BCrypt
- Session-based authentication

### Tools
- Git
- Maven
- Postman

---

##  Project Structure

```text
JobFlow
│
├── frontend
│   ├── src
│   │   ├── components
│   │   ├── pages
│   │   ├── assets
│   │   └── App.jsx
│
├── backend
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── util
│   └── config
│
└── README.md
```

---

##  Installation

### Clone Repository

```bash
git clone <repository-url>
cd JobFlow
```

### Backend Setup

```bash
cd backend
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8080
```

---

### Frontend Setup

```bash
cd frontend

npm install
npm run dev
```

Runs on:

```text
http://localhost:5173
```

---

##  Environment Variables

Backend:

```properties
groq.api.key=YOUR_API_KEY
```

---

##  Screens

Current modules:

- Dashboard:
  <img width="1890" height="853" alt="image" src="https://github.com/user-attachments/assets/09c94cd6-50cd-459e-a58a-85c9c38918b8" />

- Analyze Resume:
  <img width="642" height="852" alt="image" src="https://github.com/user-attachments/assets/2e3e04d3-a891-4457-a5a0-3bb18c2eab7d" />

- My Resumes:
  <img width="1573" height="613" alt="image" src="https://github.com/user-attachments/assets/2c61282c-b2b1-4c24-9cdd-9fb071b5ef74" />

- Resume Comparison:
  <img width="1618" height="808" alt="image" src="https://github.com/user-attachments/assets/fdbe500e-58c8-479f-828a-0bdfbe9e1c72" />

- Interview Practice:
  <img width="1637" height="857" alt="image" src="https://github.com/user-attachments/assets/45bace49-f693-4410-b398-fad0e7b5c04e" />

- Profile
  <img width="1642" height="498" alt="image" src="https://github.com/user-attachments/assets/5c53f158-93f2-4d79-94be-19a9053929c7" />


---

##  Future Improvements

- Semantic matching using embeddings
- Voice-based mock interviews
- Resume template generation
- Resume-to-job recommendation system
- AI career roadmap suggestions
- Interview analytics dashboard

---

