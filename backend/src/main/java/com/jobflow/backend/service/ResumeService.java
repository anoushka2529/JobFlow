package com.jobflow.backend.service;

import com.jobflow.backend.dto.AIProcessResponse;
import com.jobflow.backend.dto.AtsScore;
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
    private final AtsScoringService atsScoringService;

    public ResumeService(
            AIService aiService,
            AtsScoringService atsScoringService) {
        this.aiService = aiService;
        this.atsScoringService = atsScoringService;
    }

    public AIProcessResponse uploadResume(MultipartFile file, String jobDescription) throws IOException {

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

        AtsScore atsScore = atsScoringService.calculateScore(resumeData);

        String aiAnalysis = aiService.generateAIAnalysis(resumeData, atsScore, jobDescription);

        return new AIProcessResponse(
                resumeData,
                atsScore,
                aiAnalysis);
    }
}