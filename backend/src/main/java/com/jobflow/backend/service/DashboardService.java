package com.jobflow.backend.service;

import com.jobflow.backend.dto.DashboardResponse;
import com.jobflow.backend.entity.ResumeAnalysis;
import com.jobflow.backend.repository.ResumeAnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;

    public DashboardService(ResumeAnalysisRepository resumeAnalysisRepository) {
        this.resumeAnalysisRepository = resumeAnalysisRepository;
    }

    public DashboardResponse getDashboard(Long userId) {

        List<ResumeAnalysis> analyses = resumeAnalysisRepository.findByUserId(userId);

        if (analyses.isEmpty()) {
            return new DashboardResponse(
                    0,
                    0.0,
                    0,
                    0,
                    "No data yet",
                    "-",
                    "-",
                    "Analyze more resumes to generate personalized insights.",
                    "0%",
                    analyses);
        }

        analyses.sort(
                Comparator.comparing(ResumeAnalysis::getCreatedAt));

        int totalResumes = analyses.size();

        double averageScore = analyses.stream()
                .mapToInt(ResumeAnalysis::getTotalScore)
                .average()
                .orElse(0.0);

        int highestScore = analyses.stream()
                .mapToInt(ResumeAnalysis::getTotalScore)
                .max()
                .orElse(0);

        int latestScore = analyses
                .get(analyses.size() - 1)
                .getTotalScore();

        String scoreTrend = calculateTrend(analyses);
        String scoreGrowth = calculateGrowth(analyses);

        return new DashboardResponse(
                totalResumes,
                Math.round(averageScore * 100.0) / 100.0,
                highestScore,
                latestScore,
                scoreTrend,
                getStrongestArea(),
                getWeakestArea(),
                getImprovementSuggestion(),
                scoreGrowth,
                analyses);
    }

    private String calculateTrend(List<ResumeAnalysis> analyses) {

        if (analyses.size() < 2) {
            return "Not enough data";
        }

        int previousScore = analyses
                .get(analyses.size() - 2)
                .getTotalScore();

        int latestScore = analyses
                .get(analyses.size() - 1)
                .getTotalScore();

        if (latestScore > previousScore) {
            return "Improving";
        } else if (latestScore < previousScore) {
            return "Decreasing";
        } else {
            return "Stable";
        }
    }

    private String calculateGrowth(List<ResumeAnalysis> analyses) {

        if (analyses.size() < 2) {
            return "0%";
        }

        int firstScore = analyses
                .get(0)
                .getTotalScore();

        int latestScore = analyses
                .get(analyses.size() - 1)
                .getTotalScore();

        if (firstScore == 0) {
            return "0%";
        }

        double growth = ((double) (latestScore - firstScore) / firstScore) * 100;

        long roundedGrowth = Math.round(growth);

        if (roundedGrowth > 0) {
            return "+" + roundedGrowth + "%";
        }

        return roundedGrowth + "%";
    }

    private String getStrongestArea() {
        return "Projects Quality";
    }

    private String getWeakestArea() {
        return "Keyword Optimization";
    }

    private String getImprovementSuggestion() {
        return "Add stronger role-specific keywords, quantify project impact, and align resume content more closely with the target job description.";
    }
}