package com.jobflow.backend.service;

import com.jobflow.backend.dto.AuthResponse;
import com.jobflow.backend.dto.LoginRequest;
import com.jobflow.backend.dto.RegisterRequest;
import com.jobflow.backend.entity.User;
import com.jobflow.backend.exception.AuthException;
import com.jobflow.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        public AuthService(
                        UserRepository userRepository,
                        BCryptPasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        public AuthResponse register(RegisterRequest request) {

                if (request.getName() == null || request.getName().isBlank()) {
                        throw new AuthException("Name is required", HttpStatus.BAD_REQUEST);
                }

                if (!isValidEmail(request.getEmail())) {
                        throw new AuthException("Enter a valid email address", HttpStatus.BAD_REQUEST);
                }

                if (request.getPassword() == null || request.getPassword().isBlank()) {
                        throw new AuthException("Password is required", HttpStatus.BAD_REQUEST);
                }

                if (request.getPassword().length() < 6) {
                        throw new AuthException("Password must be at least 6 characters", HttpStatus.BAD_REQUEST);
                }

                String email = request.getEmail().trim().toLowerCase();

                if (userRepository.findByEmail(email).isPresent()) {
                        throw new AuthException("Email already registered", HttpStatus.CONFLICT);
                }

                User user = new User(
                                request.getName().trim(),
                                email,
                                passwordEncoder.encode(request.getPassword()));

                User savedUser = userRepository.save(user);

                return new AuthResponse(
                                savedUser.getId(),
                                savedUser.getName(),
                                savedUser.getEmail(),
                                "Registration successful");
        }

        public AuthResponse login(LoginRequest request) {

                if (!isValidEmail(request.getEmail())) {
                        throw new AuthException("Enter a valid email address", HttpStatus.BAD_REQUEST);
                }

                if (request.getPassword() == null || request.getPassword().isBlank()) {
                        throw new AuthException("Password is required", HttpStatus.BAD_REQUEST);
                }

                String email = request.getEmail().trim().toLowerCase();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new AuthException(
                                                "Invalid email or password",
                                                HttpStatus.UNAUTHORIZED));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
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

        private boolean isValidEmail(String email) {
                return email != null &&
                                email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        }
}