package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Random;

@Service
public class NotificationService {

    @Autowired(required = false)
    private ReactiveRedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();

    public NotificationService() {}

    public Flux<NotificationDTO> getRecentNotifications() {
        if (redisTemplate != null) {
            return redisTemplate.opsForList()
                .range("notifications", 0, 99)
                .cast(NotificationDTO.class);
        }
        return Flux.empty();
    }

    public void sendNotification(String category, String message) {
        NotificationDTO notification = new NotificationDTO(category, message, Instant.now());
        
        if (redisTemplate != null) {
            // Store in Redis
            redisTemplate.opsForList()
                .leftPush("notifications", notification)
                .subscribe();
            
            // Send via Redis
            redisTemplate.convertAndSend("notifications", notification).subscribe();
        }
    }

    @Scheduled(fixedRate = 30000)
    public void generateSystemNotifications() {
        if (random.nextDouble() < 0.2) { // 20% chance
            String[] categories = {"SYSTEM", "MARKET", "AGENT", "RISK"};
            String[] messages = {
                "System health check completed successfully",
                "New market data feed connected",
                "Agent performance metrics updated",
                "Risk monitoring alert threshold adjusted"
            };

            String category = categories[random.nextInt(categories.length)];
            String message = messages[random.nextInt(messages.length)];
            
            sendNotification(category, message);
        }
    }
}