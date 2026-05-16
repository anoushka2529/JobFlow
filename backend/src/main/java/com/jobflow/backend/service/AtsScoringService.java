package com.jobflow.backend.service;

import com.jobflow.backend.dto.AtsScore;
import com.jobflow.backend.dto.ResumeData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtsScoringService {

    public AtsScore calculateScore(ResumeData resumeData) {

        int skillsScore = calculateSkillsScore(resumeData.getSkills());
        int structureScore = calculateStructureScore(resumeData);
        int experienceScore = calculateExperienceScore(resumeData.getExperience());
        int projectsScore = calculateProjectsScore(resumeData.getProjects());
        int keywordScore = calculateKeywordScore(resumeData);

        int totalScore = skillsScore
                + structureScore
                + experienceScore
                + projectsScore
                + keywordScore;

        return new AtsScore(
                totalScore,
                skillsScore,
                structureScore,
                experienceScore,
                projectsScore,
                keywordScore);
    }

    private int calculateSkillsScore(List<String> skills) {

        if (skills == null || skills.isEmpty()) {
            return 0;
        }

        int skillCount = skills.size();

        if (skillCount >= 10) {
            return 20;
        } else if (skillCount >= 7) {
            return 16;
        } else if (skillCount >= 4) {
            return 12;
        } else if (skillCount >= 2) {
            return 8;
        } else {
            return 4;
        }
    }

    private int calculateStructureScore(ResumeData resumeData) {

        int score = 0;

        if (isPresent(resumeData.getName())) {
            score += 3;
        }

        if (isPresent(resumeData.getEmail())) {
            score += 3;
        }

        if (isPresent(resumeData.getProfessionalSummary())) {
            score += 4;
        }

        if (isPresent(resumeData.getEducation())) {
            score += 3;
        }

        if (isPresent(resumeData.getSkills().toString())) {
            score += 3;
        }

        if (isPresent(resumeData.getProjects())) {
            score += 4;
        }

        return Math.min(score, 20);
    }

    private int calculateExperienceScore(String experience) {

        if (!isPresent(experience)) {
            return 4;
        }

        int score = 10;

        if (containsNumber(experience)) {
            score += 4;
        }

        if (containsAny(experience, List.of(
                "developed", "built", "implemented", "designed",
                "optimized", "improved", "created", "integrated",
                "deployed", "maintained"))) {
            score += 4;
        }

        if (containsAny(experience, List.of(
                "api", "database", "frontend", "backend",
                "testing", "deployment", "performance", "scalable"))) {
            score += 2;
        }

        return Math.min(score, 20);
    }

    private int calculateProjectsScore(String projects) {

        if (!isPresent(projects)) {
            return 0;
        }

        int score = 8;

        if (containsAny(projects, List.of(
                "react", "spring boot", "java", "python", "sql",
                "postgresql", "docker", "firebase", "flask", "api"))) {
            score += 5;
        }

        if (containsAny(projects, List.of(
                "deployed", "built", "developed", "implemented",
                "integrated", "designed"))) {
            score += 4;
        }

        if (containsNumber(projects)) {
            score += 3;
        }

        return Math.min(score, 20);
    }

    private int calculateKeywordScore(ResumeData resumeData) {

        String combinedText = (resumeData.getSkills() + " " +
                resumeData.getProfessionalSummary() + " " +
                resumeData.getExperience() + " " +
                resumeData.getProjects()).toLowerCase();

        List<String> importantKeywords = List.of(
                "java",
                "spring boot",
                "rest api",
                "sql",
                "postgresql",
                "docker",
                "react",
                "git",
                "backend",
                "frontend",
                "database",
                "testing",
                "deployment",
                "agile",
                "cloud");

        int matches = 0;

        for (String keyword : importantKeywords) {
            if (combinedText.contains(keyword)) {
                matches++;
            }
        }

        double ratio = (double) matches / importantKeywords.size();

        return (int) Math.round(ratio * 20);
    }

    private boolean isPresent(String value) {
        return value != null
                && !value.isBlank()
                && !value.equalsIgnoreCase("Not Found");
    }

    private boolean containsNumber(String text) {
        return text != null && text.matches(".*\\d+.*");
    }

    private boolean containsAny(String text, List<String> words) {

        if (text == null) {
            return false;
        }

        String lowerText = text.toLowerCase();

        for (String word : words) {
            if (lowerText.contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
