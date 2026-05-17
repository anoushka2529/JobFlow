package com.jobflow.backend.dto;

import com.jobflow.backend.entity.ResumeAnalysis;
import java.util.List;

public class DashboardResponse {

    private int totalResumes;
    private double averageScore;
    private int highestScore;
    private int latestScore;
    private String scoreTrend;
    private List<ResumeAnalysis> analyses;

    public DashboardResponse(
            int totalResumes,
            double averageScore,
            int highestScore,
            int latestScore,
            String scoreTrend,
            List<ResumeAnalysis> analyses) {
        this.totalResumes = totalResumes;
        this.averageScore = averageScore;
        this.highestScore = highestScore;
        this.latestScore = latestScore;
        this.scoreTrend = scoreTrend;
        this.analyses = analyses;
    }

    public int getTotalResumes() {
        return totalResumes;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLatestScore() {
        return latestScore;
    }

    public String getScoreTrend() {
        return scoreTrend;
    }

    public List<ResumeAnalysis> getAnalyses() {
        return analyses;
    }
}