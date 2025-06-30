package com.vynqtalk.server.dto.notifications;

import com.vynqtalk.server.model.enums.NotificationType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Type is required")
    private NotificationType type;
}
// Note: Use this DTO for notification creation requests only. 