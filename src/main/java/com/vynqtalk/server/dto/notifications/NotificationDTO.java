package com.vynqtalk.server.dto.notifications;

import java.time.Instant;

import com.vynqtalk.server.model.enums.NotificationType;
import com.vynqtalk.server.model.users.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private User user;
    private String details;
    private Instant timestamp;
    private Boolean isRead;
    private NotificationType type;
}

// Note: Use this DTO for notification responses only (never for requests). 