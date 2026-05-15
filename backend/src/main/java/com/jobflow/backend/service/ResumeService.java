package com.jobflow.backend.service;

import com.jobflow.backend.dto.ResumeData;
import com.jobflow.backend.util.ResumeParserUtil;
import com.jobflow.backend.util.PdfParserUtil;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResumeService {

    private final String UPLOAD_DIR = "uploads/";

    public String uploadResume(MultipartFile file) throws IOException {

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

        System.out.println("EXTRACTED RESUME TEXT:");
        System.out.println(extractedText);

        ResumeData resumeData = ResumeParserUtil.parseResume(extractedText);

        System.out.println("PARSED RESUME DATA:");
        System.out.println("Name: " + resumeData.getName());
        System.out.println("Skills: " + resumeData.getSkills());
        System.out.println("Education: " + resumeData.getEducation());
        System.out.println("Email: " + resumeData.getEmail());
        System.out.println("Experience: " + resumeData.getExperience());
        System.out.println("Professional Summary: " + resumeData.getProfessionalSummary());
        System.out.println("Certifications: ");
        System.out.println(resumeData.getCertifications());
        System.out.println("Projects: " + resumeData.getProjects());

        return file.getOriginalFilename();
    }
}