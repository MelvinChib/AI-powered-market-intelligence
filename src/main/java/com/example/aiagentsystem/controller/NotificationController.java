package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.NotificationDTO;
import com.example.aiagentsystem.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/recent")
    public Flux<NotificationDTO> getRecentNotifications() {
        return notificationService.getRecentNotifications();
    }
}