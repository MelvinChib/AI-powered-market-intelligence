package com.example.aiagentsystem.dto;

import java.time.Instant;

public record AgentCommunicationDTO(String from, String to, String message, Instant timestamp) {}