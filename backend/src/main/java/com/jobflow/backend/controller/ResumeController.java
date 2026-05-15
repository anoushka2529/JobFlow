package com.jobflow.backend.controller;

import com.jobflow.backend.dto.UploadResponse;
import com.jobflow.backend.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public UploadResponse uploadResume(@RequestParam("file") MultipartFile file) throws Exception {

        String fileName = resumeService.uploadResume(file);

        return new UploadResponse(
                "Resume uploaded successfully",
                fileName);
    }
}