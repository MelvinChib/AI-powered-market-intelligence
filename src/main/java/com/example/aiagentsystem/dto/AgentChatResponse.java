package com.example.aiagentsystem.dto;

import java.time.Instant;

public record AgentChatResponse(String agentName, String response, Instant timestamp) {}