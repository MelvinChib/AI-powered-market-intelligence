package com.example.aiagentsystem.api.v1;

import com.example.aiagentsystem.common.constants.ApiConstants;
import com.example.aiagentsystem.domain.auth.dto.LoginRequest;
import com.example.aiagentsystem.domain.auth.dto.JwtResponse;
import com.example.aiagentsystem.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApiConstants.AUTH_ENDPOINT)
@Tag(name = "Authentication", description = "Authentication management")
public class AuthControllerV1 {

    private final AuthService authService;

    public AuthControllerV1(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public Mono<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public Mono<JwtResponse> register(@Valid @RequestBody LoginRequest request) {
        return authService.register(request);
    }
}