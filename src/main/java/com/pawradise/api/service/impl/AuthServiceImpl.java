package com.pawradise.api.service.impl;

import com.pawradise.api.dto.LoginRequest;
import com.pawradise.api.dto.RegisterRequest;
import com.pawradise.api.models.Role;
import com.pawradise.api.models.User;
import com.pawradise.api.repository.UserRepository;
import com.pawradise.api.service.AuthService;
import com.pawradise.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) { // Check for existing email
            throw new IllegalArgumentException("Email address is already taken");
        }

        // --- USERNAME GENERATION LOGIC ---
        String baseUsername = request.getEmail().split("@")[0]; // e.g., "johndoe" from "johndoe@example.com"
        String username = baseUsername;
        int counter = 1;

        // Keep checking and appending a number until the username is unique
        while(userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }
        // --- END OF LOGIC ---

        User user = new User();
        user.setUsername(username); // Set the unique username
        user.setFirstName(request.getFirstName()); // Assuming RegisterRequest has these fields
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // For simplicity, we'll return a simple token.
        // In a real app, you would generate a JWT here.
        return jwtService.generateToken(user.getUsername());
    }
}