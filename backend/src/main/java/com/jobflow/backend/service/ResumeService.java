package com.jobflow.backend.service;

import com.jobflow.backend.dto.AIProcessResponse;
import com.jobflow.backend.dto.ResumeData;
import com.jobflow.backend.util.PdfParserUtil;
import com.jobflow.backend.util.ResumeParserUtil;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResumeService {

    private final String UPLOAD_DIR = "uploads/";

    private final AIService aiService;

    public ResumeService(AIService aiService) {
        this.aiService = aiService;
    }

    public AIProcessResponse uploadResume(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        String extractedText = PdfParserUtil.extractText(path.toString());

        ResumeData resumeData = ResumeParserUtil.parseResume(extractedText);
        System.out.println(" RAW RESUME TEXT :");
        System.out.println(extractedText);

        System.out.println(" PARSED RESUME DATA :");
        System.out.println("Name: " + resumeData.getName());
        System.out.println("Email: " + resumeData.getEmail());
        System.out.println("Skills: " + resumeData.getSkills());
        System.out.println("Education: " + resumeData.getEducation());
        System.out.println("Experience: " + resumeData.getExperience());
        System.out.println("Professional Summary: " + resumeData.getProfessionalSummary());
        System.out.println("Projects: " + resumeData.getProjects());
        System.out.println("Certifications: " + resumeData.getCertifications());

        String aiAnalysis = aiService.generateAIAnalysis(resumeData);

        return new AIProcessResponse(
                resumeData,
                aiAnalysis);
    }
}