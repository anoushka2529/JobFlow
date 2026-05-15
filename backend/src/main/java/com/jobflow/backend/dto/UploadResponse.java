package com.jobflow.backend.dto;

public class UploadResponse {

    private String message;
    private String fileName;

    public UploadResponse(String message, String fileName) {
        this.message = message;
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }
}