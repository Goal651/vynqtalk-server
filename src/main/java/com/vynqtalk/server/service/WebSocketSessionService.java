package com.vynqtalk.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionService.class);
    
    // Map sessionId -> userId
    private final Map<String, String> sessionUsers = new ConcurrentHashMap<>();
    
    public void registerSession(String sessionId, String userId) {
        sessionUsers.put(sessionId, userId);
        logger.info("Registered session {} for user {}", sessionId, userId);
    }
    
    public void removeSession(String sessionId) {
        String userId = sessionUsers.remove(sessionId);
        if (userId != null) {
            logger.info("Removed session {} for user {}", sessionId, userId);
        }
    }
    
    public String getUserIdBySession(String sessionId) {
        return sessionUsers.get(sessionId);
    }
}