package com.example.aiagentsystem.domain.auth.service;

import com.example.aiagentsystem.common.enums.UserRole;
import com.example.aiagentsystem.domain.auth.dto.LoginRequest;
import com.example.aiagentsystem.domain.auth.model.User;
import com.example.aiagentsystem.domain.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthServiceImpl(userRepository, passwordEncoder, "test-secret-key-32-characters-long", 86400000L);
    }

    @Test
    void authenticate_ValidCredentials_ReturnsJwtResponse() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, UserRole.VIEWER);
        LoginRequest request = new LoginRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));

        StepVerifier.create(authService.authenticate(request))
                .expectNextMatches(response -> response.token() != null && response.type().equals("Bearer"))
                .verifyComplete();
    }

    @Test
    void register_NewUser_ReturnsJwtResponse() {
        String username = "newuser";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);
        User savedUser = new User(username, passwordEncoder.encode(password), UserRole.VIEWER);

        when(userRepository.existsByUsername(username)).thenReturn(Mono.just(false));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(authService.register(request))
                .expectNextMatches(response -> response.token() != null)
                .verifyComplete();
    }
}