package com.jobflow.backend.dto;

import java.util.List;

public class ResumeData {

    private String name;
    private List<String> skills;
    private String education;
    private String email;
    private String experience;
    private String professionalSummary;
    private String certifications;

    public ResumeData(String name, List<String> skills, String education, String email,
            String experience, String professionalSummary, String certifications) {
        this.name = name;
        this.skills = skills;
        this.education = education;
        this.email = email;
        this.experience = experience;
        this.professionalSummary = professionalSummary;
        this.certifications = certifications;
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getEducation() {
        return education;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public String getCertifications() {
        return certifications;
    }
}
