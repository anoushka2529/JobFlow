package com.jobflow.backend.dto;

public class AtsScore {

    private int totalScore;
    private int skillsScore;
    private int structureScore;
    private int experienceScore;
    private int projectsScore;
    private int keywordScore;

    public AtsScore(
            int totalScore,
            int skillsScore,
            int structureScore,
            int experienceScore,
            int projectsScore,
            int keywordScore) {
        this.totalScore = totalScore;
        this.skillsScore = skillsScore;
        this.structureScore = structureScore;
        this.experienceScore = experienceScore;
        this.projectsScore = projectsScore;
        this.keywordScore = keywordScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getSkillsScore() {
        return skillsScore;
    }

    public int getStructureScore() {
        return structureScore;
    }

    public int getExperienceScore() {
        return experienceScore;
    }

    public int getProjectsScore() {
        return projectsScore;
    }

    public int getKeywordScore() {
        return keywordScore;
    }
}
