package com.vynqtalk.server.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.vynqtalk.server.model.enums.Theme;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {
    
    private Boolean notificationEnabled;
    private Theme theme;
    private Boolean showOnlineStatus;
}
// Note: Use this DTO for user settings responses only. 