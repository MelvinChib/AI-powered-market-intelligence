package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.ComplianceLogDTO;
import com.example.aiagentsystem.dto.RiskExposureDTO;
import com.example.aiagentsystem.service.RiskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/risk/exposure")
    public Flux<RiskExposureDTO> getRiskExposure() {
        return riskService.getRiskExposure();
    }

    @GetMapping("/compliance/logs")
    public Flux<ComplianceLogDTO> getComplianceLogs() {
        return riskService.getComplianceLogs();
    }
}