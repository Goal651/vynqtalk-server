package com.vynqtalk.server.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.vynqtalk.server.service.user.UserService;
import com.vynqtalk.server.service.user.UserSettingsService;

import org.springframework.beans.factory.annotation.Autowired;

import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

    private final Map<String, String> connectedUsers = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private UserService userService;

    public WebSocketInterceptor(@Lazy SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String getSessionIdByEmail(String email) {
        return connectedUsers.get(email);
    }

    private void broadcastUsers() {
        // Only send user IDs who allow online status
        Set<Long> visibleUserIds = new HashSet<>();
        for (String email : connectedUsers.keySet()) {
            try {
                User user = userService.getUserByEmail(email);
                if (user!=null) {
                    UserSettings settings = userSettingsService.getUserSettings(user);
                    if (settings.getShowOnlineStatus() != null && settings.getShowOnlineStatus()) {
                        visibleUserIds.add(user.getId());
                    }
                }
            } catch (Exception ignored) {
                logger.error("There have been error in broadcasting online users", ignored.getCause());
            }
        }
        messagingTemplate.convertAndSend("/topic/onlineUsers", visibleUserIds);
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        Map<String, Object> attributes = accessor.getSessionAttributes();
        if (command == StompCommand.CONNECT) {
            if (attributes != null) {
                String userEmail = (String) attributes.get("userEmail");
                connectedUsers.put(userEmail, sessionId);
                logger.info("STOMP client connected: userEmail={}, sessionId={}", userEmail, sessionId);
                broadcastUsers();
            }
        } else if (command == StompCommand.DISCONNECT) {
            // Remove user by email (reverse lookup)
            String userToRemove = null;
            for (Map.Entry<String, String> entry : connectedUsers.entrySet()) {
                if (entry.getValue().equals(sessionId)) {
                    userToRemove = entry.getKey();
                    break;
                }
            }
            if (userToRemove != null) {
                connectedUsers.remove(userToRemove);
                logger.info("STOMP client disconnected: userEmail={}, sessionId={}", userToRemove, sessionId);
                broadcastUsers();
            }
        }
        return message;
    }
}