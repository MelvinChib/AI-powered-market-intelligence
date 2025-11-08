package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.JwtResponse;
import com.example.aiagentsystem.dto.LoginRequest;
import com.example.aiagentsystem.model.User;
import com.example.aiagentsystem.repository.UserRepository;
import com.example.aiagentsystem.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.username())
            .filter(user -> passwordEncoder.matches(request.password(), user.password()))
            .map(user -> {
                String token = jwtService.generateToken(user.username(), user.role());
                return ResponseEntity.ok(new JwtResponse(token, "Bearer", user.username(), user.role()));
            })
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.username())
            .hasElement()
            .flatMap(exists -> {
                if (exists) {
                    return Mono.just(ResponseEntity.badRequest().body("Username already exists"));
                }
                
                User newUser = new User(null, request.username(), 
                                      passwordEncoder.encode(request.password()), "ANALYST");
                return userRepository.save(newUser)
                    .map(user -> ResponseEntity.ok("User registered successfully"));
            });
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<JwtResponse>> refresh(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remove "Bearer " prefix
        
        if (jwtService.isTokenValid(jwt)) {
            var claims = jwtService.extractClaims(jwt);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            
            String newToken = jwtService.generateToken(username, role);
            return Mono.just(ResponseEntity.ok(new JwtResponse(newToken, "Bearer", username, role)));
        }
        
        return Mono.just(ResponseEntity.badRequest().build());
    }
}