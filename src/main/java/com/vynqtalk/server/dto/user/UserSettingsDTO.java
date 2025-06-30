package com.vynqtalk.server.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.enums.Theme;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {
    private Long id;
    private User user;
    private Boolean notificationEnabled;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Theme theme;
    private String language;
    private String timezone;
    private Boolean showOnlineStatus;
    private Boolean readReceipts;
}
// Note: Use this DTO for user settings responses only. 