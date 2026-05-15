package com.jobflow.backend.service;

import com.jobflow.backend.entity.User;
import com.jobflow.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service /*tells Spring that this class contains  business logic*/
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}