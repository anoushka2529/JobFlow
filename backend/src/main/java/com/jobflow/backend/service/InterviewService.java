package com.jobflow.backend.service;

import com.jobflow.backend.dto.InterviewEvaluateRequest;
import com.jobflow.backend.dto.InterviewEvaluateResponse;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {

    private final AIService aiService;

    public InterviewService(AIService aiService) {
        this.aiService = aiService;
    }

    public InterviewEvaluateResponse evaluateAnswer(InterviewEvaluateRequest request) {

        String prompt = """
                You are an interview evaluator.

                Question:
                %s

                Candidate Answer:
                %s

                Rate the answer out of 10.
                Give concise improvement feedback.

                Return strictly in this format:
                Score: <number out of 10>
                Feedback: <feedback points>
                """.formatted(request.getQuestion(), request.getAnswer());

        String aiResponse = aiService.generateText(prompt);

        int score = extractScore(aiResponse);

        return new InterviewEvaluateResponse(score, aiResponse);
    }

    private int extractScore(String aiResponse) {
        try {
            String[] lines = aiResponse.split("\\n");

            for (String line : lines) {
                if (line.toLowerCase().contains("score")) {
                    String numberOnly = line.replaceAll("[^0-9]", "");

                    if (!numberOnly.isEmpty()) {
                        return Math.min(Integer.parseInt(numberOnly), 10);
                    }
                }
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }
}
