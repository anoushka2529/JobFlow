package com.jobflow.backend.service;

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

        return file.getOriginalFilename();
    }
}