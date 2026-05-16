package com.jobflow.backend.service;

import com.jobflow.backend.dto.AtsScore;
import com.jobflow.backend.dto.ResumeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

        @Value("${groq.api.key}")
        private String apiKey;

        private final WebClient webClient;

        public AIService(WebClient.Builder builder) {
                this.webClient = builder
                                .baseUrl("https://api.groq.com")
                                .build();
        }

        public String generateAIAnalysis(
                        ResumeData resumeData,
                        AtsScore atsScore,
                        String jobDescription) {

                String prompt = """
                                You are a senior technical recruiter, ATS optimization specialist,
                                and software engineering interview coach.

                                The ATS score below is already calculated by the backend.
                                Do not invent a new score. Explain the given score.

                                ATS SCORING METHOD:
                                Total score is out of 100.
                                - Skills Match: 20 points
                                - Resume Structure: 20 points
                                - Experience Quality: 20 points
                                - Projects Quality: 20 points
                                - Keyword Optimization: 20 points

                                CALCULATED ATS SCORE:
                                Overall Score: %d/100
                                Skills Score: %d/20
                                Structure Score: %d/20
                                Experience Score: %d/20
                                Projects Score: %d/20
                                Keyword Score: %d/20

                                TARGET JOB DESCRIPTION:
                                %s

                                TASK:
                                Analyze the candidate profile against the target job description.

                                Return the response in this structured format:

                                ======================
                                ATS SCORE EXPLANATION
                                ======================
                                - Explain why this score makes sense.
                                - Mention where the resume matches the JD.
                                - Mention where points were lost.

                                ======================
                                RESUME STRENGTHS
                                ======================
                                - Mention the strongest parts of the resume.

                                ======================
                                AREAS FOR IMPROVEMENT
                                ======================
                                - Mention weak or incomplete sections.

                                ======================
                                ATS OPTIMIZATION
                                ======================
                                - Suggest missing JD keywords.
                                - Suggest formatting or section improvements.
                                - Suggest role-specific improvements.

                                ======================
                                BETTER BULLET POINT SUGGESTIONS
                                ======================
                                - Rewrite weak experience/project points using stronger wording.
                                - Add measurable impact where appropriate.
                                - Do not invent fake achievements.

                                ======================
                                SKILLS GAP ANALYSIS
                                ======================
                                - Compare resume skills with job description requirements.
                                - Mention missing or weakly represented skills.

                                ======================
                                PERSONALIZED INTERVIEW QUESTIONS
                                ======================
                                Generate 6-8 strong personalized interview questions:
                                - technical questions
                                - project-based questions
                                - behavioral questions
                                - backend/system design questions if relevant
                                - questions should be aligned with the target JD

                                IMPORTANT RULES:
                                - Do not invent fake experience.
                                - Be specific and practical.
                                - Keep feedback concise but useful.
                                - Focus on software engineering/product-company roles.

                                CANDIDATE PROFILE:

                                Name: %s
                                Skills: %s
                                Summary: %s
                                Experience: %s
                                Projects: %s
                                Education: %s
                                Certifications: %s
                                """.formatted(
                                atsScore.getTotalScore(),
                                atsScore.getSkillsScore(),
                                atsScore.getStructureScore(),
                                atsScore.getExperienceScore(),
                                atsScore.getProjectsScore(),
                                atsScore.getKeywordScore(),
                                jobDescription == null || jobDescription.isBlank()
                                                ? "No job description provided."
                                                : jobDescription,
                                resumeData.getName(),
                                resumeData.getSkills(),
                                resumeData.getProfessionalSummary(),
                                resumeData.getExperience(),
                                resumeData.getProjects(),
                                resumeData.getEducation(),
                                resumeData.getCertifications());

                return callGroq(prompt);
        }

        @SuppressWarnings("unchecked")
        private String callGroq(String prompt) {

                try {

                        Map<String, Object> requestBody = Map.of(
                                        "model", "llama-3.3-70b-versatile",
                                        "messages", List.of(
                                                        Map.of(
                                                                        "role", "user",
                                                                        "content", prompt)),
                                        "temperature", 0.4);

                        Map<String, Object> response = webClient.post()
                                        .uri("/openai/v1/chat/completions")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(requestBody)
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .block();

                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

                        Map<String, Object> firstChoice = choices.get(0);

                        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

                        return message.get("content").toString();

                } catch (Exception e) {

                        e.printStackTrace();

                        return "AI analysis generation failed.";
                }
        }
}