package com.jobflow.backend.dto;

public class AIProcessResponse {

    private ResumeData resumeData;
    private AtsScore atsScore;
    private String aiAnalysis;

    public AIProcessResponse(
            ResumeData resumeData,
            AtsScore atsScore,
            String aiAnalysis) {
        this.resumeData = resumeData;
        this.atsScore = atsScore;
        this.aiAnalysis = aiAnalysis;
    }

    public ResumeData getResumeData() {
        return resumeData;
    }

    public AtsScore getAtsScore() {
        return atsScore;
    }

    public String getAiAnalysis() {
        return aiAnalysis;
    }
}