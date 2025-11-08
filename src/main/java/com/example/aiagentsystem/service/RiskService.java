package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.ComplianceLogDTO;
import com.example.aiagentsystem.dto.RiskAlertDTO;
import com.example.aiagentsystem.dto.RiskExposureDTO;
import com.example.aiagentsystem.model.ComplianceLog;
import com.example.aiagentsystem.repository.ComplianceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class RiskService {

    private final ComplianceLogRepository complianceLogRepository;
    @Autowired(required = false)
    private ReactiveRedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();

    public RiskService(ComplianceLogRepository complianceLogRepository) {
        this.complianceLogRepository = complianceLogRepository;
    }

    public Flux<RiskExposureDTO> getRiskExposure() {
        return Flux.fromIterable(List.of(
            new RiskExposureDTO("Technology", 0.35 + random.nextGaussian() * 0.05, 0.7 + random.nextGaussian() * 0.1),
            new RiskExposureDTO("Healthcare", 0.25 + random.nextGaussian() * 0.03, 0.5 + random.nextGaussian() * 0.1),
            new RiskExposureDTO("Finance", 0.20 + random.nextGaussian() * 0.02, 0.6 + random.nextGaussian() * 0.1),
            new RiskExposureDTO("Energy", 0.20 + random.nextGaussian() * 0.04, 0.8 + random.nextGaussian() * 0.1)
        ));
    }

    public Flux<ComplianceLogDTO> getComplianceLogs() {
        return complianceLogRepository.findAll()
            .map(log -> new ComplianceLogDTO(log.agent(), log.rule(), log.severity(), log.timestamp()));
    }

    @Scheduled(fixedRate = 10000)
    public void monitorRisk() {
        if (random.nextDouble() < 0.3) { // 30% chance of alert
            String[] levels = {"HIGH", "MEDIUM", "LOW"};
            String[] sectors = {"Technology", "Healthcare", "Finance", "Energy"};
            String[] reasons = {
                "Position size exceeds limit",
                "Volatility threshold breached",
                "Correlation risk detected",
                "Liquidity concern"
            };

            RiskAlertDTO alert = new RiskAlertDTO(
                levels[random.nextInt(levels.length)],
                sectors[random.nextInt(sectors.length)],
                reasons[random.nextInt(reasons.length)],
                Instant.now()
            );

            if (redisTemplate != null) {
                redisTemplate.convertAndSend("risk-alerts", alert).subscribe();
            }

            // Log compliance violation
            complianceLogRepository.save(new ComplianceLog(
                null, "agent-" + (random.nextInt(3) + 1), 
                "Risk Limit", alert.level(), Instant.now()
            )).subscribe();
        }
    }
}