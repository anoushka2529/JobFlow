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

        int latestScore = analyses.get(analyses.size() - 1).getTotalScore();

        String scoreTrend = calculateTrend(analyses);

        return new DashboardResponse(
                totalResumes,
                Math.round(averageScore * 100.0) / 100.0,
                highestScore,
                latestScore,
                scoreTrend,
                analyses);
    }

    private String calculateTrend(List<ResumeAnalysis> analyses) {

        if (analyses.size() < 2) {
            return "Not enough data";
        }

        int previousScore = analyses.get(analyses.size() - 2).getTotalScore();

        int latestScore = analyses.get(analyses.size() - 1).getTotalScore();

        if (latestScore > previousScore) {
            return "Improving";
        } else if (latestScore < previousScore) {
            return "Decreasing";
        } else {
            return "Stable";
        }
    }
}