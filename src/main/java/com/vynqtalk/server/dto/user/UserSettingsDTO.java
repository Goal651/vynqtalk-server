package com.vynqtalk.server.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.vynqtalk.server.model.enums.Theme;
import com.vynqtalk.server.model.users.User;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {
    private Long id;
    private User user;
    private Boolean notificationEnabled;
    private Theme theme;
    private Boolean showOnlineStatus;
}
// Note: Use this DTO for user settings responses only. 