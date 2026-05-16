package com.jobflow.backend.controller;

import com.jobflow.backend.dto.AIProcessResponse;
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
    public AIProcessResponse uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "jobDescription", required = false) String jobDescription) throws Exception {
        return resumeService.uploadResume(file, jobDescription);
    }
}