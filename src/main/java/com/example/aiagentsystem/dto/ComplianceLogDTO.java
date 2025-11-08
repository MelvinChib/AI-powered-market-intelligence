package com.example.aiagentsystem.dto;

import java.time.Instant;

public record ComplianceLogDTO(String agent, String rule, String severity, Instant timestamp) {}