package com.pawradise.api.controller;

import com.pawradise.api.dto.AuthResponse;
import com.pawradise.api.dto.LoginRequest;
import com.pawradise.api.dto.RegisterRequest;
import com.pawradise.api.models.User;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(@Autowired AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        User newUser = authService.register(request);
        return new ResponseEntity<>(ApiResponse.success("User registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid  @RequestBody LoginRequest request) {
        String token = authService.login(request);
        AuthResponse authResponse = new AuthResponse(token, request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }
}