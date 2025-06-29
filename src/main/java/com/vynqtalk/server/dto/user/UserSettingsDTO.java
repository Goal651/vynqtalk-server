package com.vynqtalk.server.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {
    private Boolean notificationEnabled;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private String theme;
    private String language;
    private String timezone;
    private Boolean showOnlineStatus;
    private Boolean readReceipts;
}
// Note: Use this DTO for user settings responses only. 