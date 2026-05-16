package com.jobflow.backend.service;

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

        public String generateAIAnalysis(ResumeData resumeData) {

                String prompt = """
                                You are a senior technical recruiter, ATS specialist,
                                and software engineering interview coach.

                                TASK:
                                Analyze this candidate profile and provide:

                                1. Resume strengths
                                2. Resume improvement suggestions
                                3. ATS optimization suggestions
                                4. Better wording suggestions
                                5. 6-8 personalized interview questions

                                RULES:
                                - Do not invent fake experience
                                - Be specific
                                - Keep output structured
                                - Focus on software/backend/full-stack roles
                                - Use concise but professional language

                                Candidate:

                                Name: %s
                                Skills: %s
                                Summary: %s
                                Experience: %s
                                Projects: %s
                                Education: %s
                                Certifications: %s
                                """.formatted(
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
                                        .header(HttpHeaders.AUTHORIZATION,
                                                        "Bearer " + apiKey)
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