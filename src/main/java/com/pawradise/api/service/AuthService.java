package com.pawradise.api.service;

import com.pawradise.api.dto.LoginRequest;
import com.pawradise.api.dto.RegisterRequest;
import com.pawradise.api.models.User;

public interface AuthService {
    User register(RegisterRequest request);
    String login(LoginRequest request);
}
