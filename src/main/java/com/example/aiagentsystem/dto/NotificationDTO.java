package com.example.aiagentsystem.dto;

import java.time.Instant;

public record NotificationDTO(String category, String message, Instant timestamp) {}