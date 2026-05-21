package com.jobflow.backend.controller;

import com.jobflow.backend.dto.InterviewEvaluateRequest;
import com.jobflow.backend.dto.InterviewEvaluateResponse;
import com.jobflow.backend.service.InterviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interview")
@CrossOrigin(origins = "http://localhost:5173")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/evaluate")
    public InterviewEvaluateResponse evaluateAnswer(
            @RequestBody InterviewEvaluateRequest request) {
        return interviewService.evaluateAnswer(request);
    }
}