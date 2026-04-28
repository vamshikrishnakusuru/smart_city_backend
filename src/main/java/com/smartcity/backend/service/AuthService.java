package com.smartcity.backend.service;

import com.smartcity.backend.dto.auth.AuthResponse;
import com.smartcity.backend.dto.auth.LoginRequest;
import com.smartcity.backend.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
