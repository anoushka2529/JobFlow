package com.jobflow.backend.service;

import com.jobflow.backend.dto.AtsScore;
import com.jobflow.backend.dto.ResumeData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AtsScoringService {

    public AtsScore calculateScore(ResumeData resumeData, String jobDescription) {

        int skillsScore = calculateSkillsScore(resumeData.getSkills(), jobDescription);
        int structureScore = calculateStructureScore(resumeData);
        int experienceScore = calculateExperienceScore(resumeData.getExperience(), jobDescription);
        int projectsScore = calculateProjectsScore(resumeData.getProjects(), jobDescription);
        int keywordScore = calculateKeywordScore(resumeData, jobDescription);

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

    private int calculateSkillsScore(List<String> skills, String jobDescription) {

        if (skills == null || skills.isEmpty()) {
            return 0;
        }

        if (!isPresent(jobDescription)) {
            return Math.min(skills.size() * 2, 20);
        }

        String lowerJobDescription = jobDescription.toLowerCase();

        int matchedSkills = 0;

        for (String skill : skills) {
            if (skill != null && lowerJobDescription.contains(skill.toLowerCase())) {
                matchedSkills++;
            }
        }

        double ratio = (double) matchedSkills / skills.size();

        return (int) Math.round(ratio * 20);
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

        if (resumeData.getSkills() != null && !resumeData.getSkills().isEmpty()) {
            score += 3;
        }

        if (isPresent(resumeData.getProjects())) {
            score += 4;
        }

        return Math.min(score, 20);
    }

    private int calculateExperienceScore(String experience, String jobDescription) {

        if (!isPresent(experience)) {
            return 4;
        }

        int score = 8;

        if (containsNumber(experience)) {
            score += 3;
        }

        if (containsAny(experience, List.of(
                "developed", "built", "implemented", "designed",
                "optimized", "improved", "created", "integrated",
                "deployed", "maintained"))) {
            score += 3;
        }

        if (isPresent(jobDescription)) {
            int jdMatches = countKeywordOverlap(experience, jobDescription);
            score += Math.min(jdMatches, 6);
        }

        return Math.min(score, 20);
    }

    private int calculateProjectsScore(String projects, String jobDescription) {

        if (!isPresent(projects)) {
            return 0;
        }

        int score = 7;

        if (containsAny(projects, List.of(
                "react", "spring boot", "java", "python", "sql",
                "postgresql", "docker", "firebase", "flask", "api"))) {
            score += 4;
        }

        if (containsAny(projects, List.of(
                "deployed", "built", "developed", "implemented",
                "integrated", "designed"))) {
            score += 3;
        }

        if (containsNumber(projects)) {
            score += 2;
        }

        if (isPresent(jobDescription)) {
            int jdMatches = countKeywordOverlap(projects, jobDescription);
            score += Math.min(jdMatches, 4);
        }

        return Math.min(score, 20);
    }

    private int calculateKeywordScore(ResumeData resumeData, String jobDescription) {

        String resumeText = buildResumeText(resumeData);

        if (!isPresent(jobDescription)) {
            return 0;
        }

        List<String> jdKeywords = extractKeywords(jobDescription);

        if (jdKeywords.isEmpty()) {
            return 0;
        }

        int matches = 0;

        for (String keyword : jdKeywords) {
            if (resumeText.contains(keyword)) {
                matches++;
            }
        }

        double ratio = (double) matches / jdKeywords.size();

        return (int) Math.round(ratio * 20);
    }

    private int countKeywordOverlap(String sectionText, String jobDescription) {

        if (!isPresent(sectionText) || !isPresent(jobDescription)) {
            return 0;
        }

        String lowerSection = sectionText.toLowerCase();
        List<String> jdKeywords = extractKeywords(jobDescription);

        int matches = 0;

        for (String keyword : jdKeywords) {
            if (lowerSection.contains(keyword)) {
                matches++;
            }
        }

        return matches;
    }

    private List<String> extractKeywords(String text) {

        List<String> stopWords = List.of(
                "the", "and", "or", "with", "for", "you", "your",
                "are", "our", "this", "that", "will", "from", "have",
                "has", "was", "were", "been", "their", "they", "them",
                "role", "candidate", "responsibilities", "requirements",
                "experience", "skills", "knowledge", "ability", "work",
                "team", "good", "strong", "excellent", "plus", "using",
                "based", "such", "etc", "in", "on", "to", "of", "a",
                "an", "as", "is", "be", "by", "at");

        String cleanedText = text.toLowerCase().replaceAll("[^a-zA-Z0-9+#. ]", " ");

        String[] words = cleanedText.split("\\s+");

        List<String> keywords = new ArrayList<>();

        for (String word : words) {
            if (word.length() < 3) {
                continue;
            }

            if (stopWords.contains(word)) {
                continue;
            }

            if (!keywords.contains(word)) {
                keywords.add(word);
            }
        }

        return keywords;
    }

    private String buildResumeText(ResumeData resumeData) {

        return (safeText(resumeData.getSkills()) + " " +
                safeText(resumeData.getProfessionalSummary()) + " " +
                safeText(resumeData.getExperience()) + " " +
                safeText(resumeData.getProjects()) + " " +
                safeText(resumeData.getEducation())).toLowerCase();
    }

    private String safeText(Object value) {
        return value == null ? "" : value.toString();
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