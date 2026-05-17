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
import com.jobflow.backend.entity.ResumeAnalysis;
import com.jobflow.backend.repository.ResumeAnalysisRepository;
import java.util.List;

@Service
public class ResumeService {

    private final String UPLOAD_DIR = "uploads/";
    private final ResumeAnalysisRepository resumeAnalysisRepository;

    private final AIService aiService;
    private final AtsScoringService atsScoringService;

    public ResumeService(
            AIService aiService,
            AtsScoringService atsScoringService,
            ResumeAnalysisRepository resumeAnalysisRepository) {
        this.aiService = aiService;
        this.atsScoringService = atsScoringService;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
    }

    public List<ResumeAnalysis> getAnalysisHistory() {
        return resumeAnalysisRepository.findAll();
    }

    public List<ResumeAnalysis> getAnalysisHistoryByUser(Long userId) {
        return resumeAnalysisRepository.findByUserId(userId);
    }

    public AIProcessResponse uploadResume(MultipartFile file, String jobDescription, Long userId) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        if (jobDescription == null || jobDescription.isBlank()) {
            throw new RuntimeException("Job description is required");
        }

        if (userId == null) {
            throw new RuntimeException("User ID is required");
        }

        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        String extractedText = PdfParserUtil.extractText(path.toString());

        ResumeData resumeData = ResumeParserUtil.parseResume(extractedText);

        AtsScore atsScore = atsScoringService.calculateScore(resumeData);

        String aiAnalysis = aiService.generateAIAnalysis(resumeData, atsScore, jobDescription);

        ResumeAnalysis savedAnalysis = new ResumeAnalysis(
                userId,
                file.getOriginalFilename(),
                resumeData.getName(),
                resumeData.getEmail(),
                jobDescription,
                atsScore.getTotalScore(),
                aiAnalysis);

        resumeAnalysisRepository.save(savedAnalysis);
        return new AIProcessResponse(
                resumeData,
                atsScore,
                aiAnalysis);
    }
}