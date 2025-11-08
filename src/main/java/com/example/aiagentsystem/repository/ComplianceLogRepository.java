package com.example.aiagentsystem.repository;

import com.example.aiagentsystem.model.ComplianceLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ComplianceLogRepository extends R2dbcRepository<ComplianceLog, Long> {
}