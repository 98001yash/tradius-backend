package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.AuthResponse;
import com.company.tradius_backend.dtos.LoginRequest;
import com.company.tradius_backend.dtos.SignupRequest;

public interface AuthService {


    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
}
