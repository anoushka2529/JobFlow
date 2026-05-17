# JobFlow AI

## Overview
Short paragraph:
JobFlow AI is an AI-powered resume analysis platform that evaluates resumes against job descriptions, generates ATS scores, provides personalized feedback, identifies improvement areas, and offers interview preparation insights.

## Features
- User registration and login
- Secure password storage using BCrypt
- Resume upload (PDF)
- ATS score generation
- Resume strengths analysis
- Areas for improvement
- ATS optimization suggestions
- Better bullet-point recommendations
- Skills gap analysis
- Personalized interview questions
- User-specific dashboard
- Resume history tracking
- AI-powered insights and trends

## Tech Stack

Frontend:
- React
- Vite
- CSS

Backend:
- Spring Boot
- Java
- REST APIs

Database:
- PostgreSQL

AI:
- Groq API (Llama model)

## Architecture Flow

Login/Register
↓
Upload Resume + Job Description
↓
Resume Parsing
↓
ATS Score Calculation
↓
AI Analysis
↓
Save Analysis to PostgreSQL
↓
Dashboard Insights

## Screenshots
(Add screenshots here)

## Setup Instructions

Backend:
1. Clone repository
2. Configure PostgreSQL
3. Add API keys
4. Run:

mvnw spring-boot:run

Frontend:
1. Navigate to frontend folder
2. Run:

npm install
npm run dev

## Future Improvements
- Resume score trend graphs
- Deploy on cloud
- Role-specific AI recommendations
- Export reports as PDF
