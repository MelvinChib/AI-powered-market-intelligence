package com.example.aiagentsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.Instant;

@Table("agent_insights")
public record AgentInsight(
    @Id Long id,
    String agentName,
    String message,
    String sentiment,
    Instant timestamp
) {}