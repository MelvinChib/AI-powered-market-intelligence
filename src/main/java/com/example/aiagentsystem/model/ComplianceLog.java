package com.example.aiagentsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.Instant;

@Table("compliance_logs")
public record ComplianceLog(
    @Id Long id,
    String agent,
    String rule,
    String severity,
    Instant timestamp
) {}