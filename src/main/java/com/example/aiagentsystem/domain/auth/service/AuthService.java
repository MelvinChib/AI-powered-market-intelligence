package com.example.aiagentsystem.domain.auth.service;

import com.example.aiagentsystem.domain.auth.dto.LoginRequest;
import com.example.aiagentsystem.domain.auth.dto.JwtResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<JwtResponse> authenticate(LoginRequest request);
    Mono<JwtResponse> register(LoginRequest request);
    Mono<Boolean> validateToken(String token);
}