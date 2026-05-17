package com.jobflow.backend.dto;

import com.jobflow.backend.entity.ResumeAnalysis;
import java.util.List;

public class DashboardResponse {

    private int totalResumes;
    private double averageScore;
    private int highestScore;
    private int latestScore;
    private String scoreTrend;

    private String strongestArea;
    private String weakestArea;
    private String improvementSuggestion;
    private String scoreGrowth;

    private List<ResumeAnalysis> analyses;

    public DashboardResponse(
            int totalResumes,
            double averageScore,
            int highestScore,
            int latestScore,
            String scoreTrend,
            String strongestArea,
            String weakestArea,
            String improvementSuggestion,
            String scoreGrowth,
            List<ResumeAnalysis> analyses) {
        this.totalResumes = totalResumes;
        this.averageScore = averageScore;
        this.highestScore = highestScore;
        this.latestScore = latestScore;
        this.scoreTrend = scoreTrend;
        this.strongestArea = strongestArea;
        this.weakestArea = weakestArea;
        this.improvementSuggestion = improvementSuggestion;
        this.scoreGrowth = scoreGrowth;
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

    public String getStrongestArea() {
        return strongestArea;
    }

    public String getWeakestArea() {
        return weakestArea;
    }

    public String getImprovementSuggestion() {
        return improvementSuggestion;
    }

    public String getScoreGrowth() {
        return scoreGrowth;
    }

    public List<ResumeAnalysis> getAnalyses() {
        return analyses;
    }
}