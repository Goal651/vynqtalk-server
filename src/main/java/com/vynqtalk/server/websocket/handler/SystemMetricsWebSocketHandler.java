package com.vynqtalk.server.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vynqtalk.server.dto.SystemMetric;
import com.vynqtalk.server.service.SystemMetricsService;

import org.springframework.lang.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SystemMetricsWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private final SystemMetricsService systemMetricsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SystemMetricsWebSocketHandler(SystemMetricsService systemMetricsService) {
        this.systemMetricsService = systemMetricsService;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
    }

    // Broadcast metrics every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void broadcastMetrics() throws Exception {
        List<SystemMetric> metrics = systemMetricsService.getSystemMetrics();
        String metricsJson = objectMapper.writeValueAsString(metrics);
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(metricsJson));
                }
            }
        }
    }
}