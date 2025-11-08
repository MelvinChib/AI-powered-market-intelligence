package com.example.aiagentsystem.dto;

import java.time.Instant;

public record RiskAlertDTO(String level, String sector, String reason, Instant timestamp) {}