package com.jobflow.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_analysis")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    Long userId;
    private String fileName;
    private String candidateName;
    private String candidateEmail;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private int totalScore;
    private int skillsScore;
    private int structureScore;
    private int experienceScore;
    private int projectsScore;
    private int keywordScore;

    @Column(columnDefinition = "TEXT")
    private String aiAnalysis;

    private LocalDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String interviewQuestions;

    public ResumeAnalysis() {
    }

    public ResumeAnalysis(
            Long userId,
            String fileName,
            String candidateName,
            String candidateEmail,
            String jobDescription,
            int totalScore,
            int skillsScore,
            int structureScore,
            int experienceScore,
            int projectsScore,
            int keywordScore,
            String aiAnalysis,
            String interviewQuestions) {
        this.userId = userId;
        this.fileName = fileName;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.jobDescription = jobDescription;

        this.totalScore = totalScore;
        this.skillsScore = skillsScore;
        this.structureScore = structureScore;
        this.experienceScore = experienceScore;
        this.projectsScore = projectsScore;
        this.keywordScore = keywordScore;

        this.aiAnalysis = aiAnalysis;
        this.createdAt = LocalDateTime.now();
        this.interviewQuestions=interviewQuestions;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getAiAnalysis() {
        return aiAnalysis;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
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
    public String getInterviewQuestions() {
        return interviewQuestions;
    }
}