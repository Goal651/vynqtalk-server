package com.vynqtalk.server.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsUpdateRequest {
    @NotNull(message = "Notification enabled is required")
    private Boolean notificationEnabled;

    @NotNull(message = "Email notifications is required")
    private Boolean emailNotifications;

    @NotNull(message = "Push notifications is required")
    private Boolean pushNotifications;

    @NotBlank(message = "Theme is required")
    private String theme;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Timezone is required")
    private String timezone;

    @NotNull(message = "Show online status is required")
    private Boolean showOnlineStatus;

    @NotNull(message = "Read receipts is required")
    private Boolean readReceipts;
}

// Note: Use this DTO for user settings update requests only. 