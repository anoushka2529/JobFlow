package com.jobflow.backend.dto;

public class AIProcessResponse {

    private ResumeData resumeData;
    private String aiAnalysis;

    public AIProcessResponse(ResumeData resumeData, String aiAnalysis) {
        this.resumeData = resumeData;
        this.aiAnalysis = aiAnalysis;
    }

    public ResumeData getResumeData() {
        return resumeData;
    }

    public String getaiAnalysis() {
        return aiAnalysis;
    }
}
