package com.jobflow.backend.dto;

public class InterviewEvaluateResponse {

    private int score;
    private String feedback;

    public InterviewEvaluateResponse(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }
}
