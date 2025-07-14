package com.vynqtalk.server.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.dto.admin.SystemMetric;

@Service
public class SystemMetricsBroadcaster {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SystemMetricsService systemMetricsService;

    @Scheduled(fixedRate = 5000)
    public void broadcastMetrics() {
        List<SystemMetric> metrics = systemMetricsService.getSystemMetrics();
        messagingTemplate.convertAndSend("/topic/systemMetrics", metrics);
    }
} 