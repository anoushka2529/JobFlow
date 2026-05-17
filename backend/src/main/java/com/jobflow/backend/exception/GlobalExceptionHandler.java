package com.jobflow.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(AuthException.class)
        public ResponseEntity<Map<String, String>> handleAuthException(
                        AuthException ex) {

                Map<String, String> response = new HashMap<>();

                response.put("message", ex.getMessage());

                return new ResponseEntity<>(
                                response,
                                ex.getStatus());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleGenericException(
                        Exception ex) {

                Map<String, String> response = new HashMap<>();

                response.put("message", ex.getMessage());

                return new ResponseEntity<>(
                                response,
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
