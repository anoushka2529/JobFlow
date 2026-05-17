package com.jobflow.backend.service;

import com.jobflow.backend.dto.AuthResponse;
import com.jobflow.backend.dto.LoginRequest;
import com.jobflow.backend.dto.RegisterRequest;
import com.jobflow.backend.entity.User;
import com.jobflow.backend.exception.AuthException;
import com.jobflow.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(
            RegisterRequest request) {

        if (request.getName() == null ||
                request.getName().isBlank()) {

            throw new AuthException(
                    "Name is required",
                    HttpStatus.BAD_REQUEST);
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new AuthException(
                    "Email is required",
                    HttpStatus.BAD_REQUEST);
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new AuthException(
                    "Password is required",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(
                request.getEmail()).isPresent()) {

            throw new AuthException(
                    "Email already registered",
                    HttpStatus.CONFLICT);
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword());

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "Registration successful");
    }

    public AuthResponse login(
            LoginRequest request) {

        User user = userRepository.findByEmail(
                request.getEmail())
                .orElseThrow(
                        () -> new AuthException(
                                "Invalid email or password",
                                HttpStatus.UNAUTHORIZED));

        if (!user.getPassword()
                .equals(
                        request.getPassword())) {

            throw new AuthException(
                    "Invalid email or password",
                    HttpStatus.UNAUTHORIZED);
        }

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                "Login successful");
    }
}