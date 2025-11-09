package com.example.aiagentsystem.domain.auth.service;

import com.example.aiagentsystem.common.enums.UserRole;
import com.example.aiagentsystem.common.exception.BusinessException;
import com.example.aiagentsystem.domain.auth.dto.JwtResponse;
import com.example.aiagentsystem.domain.auth.dto.LoginRequest;
import com.example.aiagentsystem.domain.auth.model.User;
import com.example.aiagentsystem.domain.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String jwtSecret;
    private final long jwtExpiration;

    public AuthServiceImpl(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          @Value("${spring.security.jwt.secret}") String jwtSecret,
                          @Value("${spring.security.jwt.expiration}") long jwtExpiration) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    @Override
    public Mono<JwtResponse> authenticate(LoginRequest request) {
        return userRepository.findByUsername(request.username())
                .switchIfEmpty(Mono.error(new BusinessException("Invalid credentials", HttpStatus.UNAUTHORIZED)))
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new BusinessException("Invalid credentials", HttpStatus.UNAUTHORIZED)))
                .map(this::generateToken);
    }

    @Override
    public Mono<JwtResponse> register(LoginRequest request) {
        return userRepository.existsByUsername(request.username())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException("Username already exists", HttpStatus.CONFLICT));
                    }
                    User user = new User(request.username(), passwordEncoder.encode(request.password()), UserRole.VIEWER);
                    return userRepository.save(user);
                })
                .map(this::generateToken);
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return Mono.just(true);
        } catch (Exception e) {
            return Mono.just(false);
        }
    }

    private JwtResponse generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().getAuthority())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
        
        return new JwtResponse(token, "Bearer", jwtExpiration / 1000);
    }
}