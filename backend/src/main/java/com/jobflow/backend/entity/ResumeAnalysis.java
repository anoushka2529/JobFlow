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

    @Column(columnDefinition = "TEXT")
    private String aiAnalysis;

    private LocalDateTime createdAt;

    public ResumeAnalysis() {
    }

    public ResumeAnalysis(
            Long userId,
            String fileName,
            String candidateName,
            String candidateEmail,
            String jobDescription,
            int totalScore,
            String aiAnalysis) {
        this.userId = userId;
        this.fileName = fileName;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.jobDescription = jobDescription;
        this.totalScore = totalScore;
        this.aiAnalysis = aiAnalysis;
        this.createdAt = LocalDateTime.now();
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
}