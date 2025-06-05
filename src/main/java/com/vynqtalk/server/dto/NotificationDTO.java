package com.vynqtalk.server.dto;

import java.time.Instant;

import com.vynqtalk.server.model.User;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private User user;
    private String message;
    private Instant timestamp;
    private Boolean isRead;
    private String type;
} 