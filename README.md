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
<img width="485" height="433" alt="image" src="https://github.com/user-attachments/assets/58042875-6dc4-4aa3-94e6-5491014c0f26" />
<img width="990" height="810" alt="image" src="https://github.com/user-attachments/assets/e9400a61-8301-46ad-b9c9-c01e49c7f568" />
<img width="1002" height="722" alt="image" src="https://github.com/user-attachments/assets/14fdfa50-e903-4b07-a628-a9404009efab" />
<img width="742" height="792" alt="image" src="https://github.com/user-attachments/assets/77cb90a8-60c1-4fb3-9717-0fc6da2cccf8" />
<img width="743" height="650" alt="image" src="https://github.com/user-attachments/assets/28f9a5e7-a21c-460c-a49e-aa9813766a6f" />
<img width="743" height="822" alt="image" src="https://github.com/user-attachments/assets/c275fc41-1c7b-49fe-b596-3ff5ddbee090" />
<img width="743" height="858" alt="image" src="https://github.com/user-attachments/assets/253199d0-f601-48b3-b901-272af20e34d1" />

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
