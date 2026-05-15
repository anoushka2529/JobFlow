package com.jobflow.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public AIService(WebClient.Builder builder) {

        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateInterviewQuestions(String resumeText) {

        String prompt = """
                You are an AI interview coach.

                Based on the following resume, generate:

                1. 5 technical interview questions
                2. 3 HR interview questions
                3. 2 project-specific questions

                Keep the questions personalized to the candidate’s skills,
                experience, and projects.

                Resume:
                """ + resumeText;

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of(
                                "parts", new Object[] {
                                        Map.of("text", prompt)
                                })
                });

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.0-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}