package com.example.aiagentsystem.dto;

import java.time.Instant;

public record AgentInsightDTO(String agentName, String message, String sentiment, Instant timestamp) {}