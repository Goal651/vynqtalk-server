package com.vynqtalk.server.dto.user;

import com.vynqtalk.server.model.enums.Theme;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsUpdateRequest {
    private Boolean notificationEnabled;
    private Theme theme;
    private Boolean showOnlineStatus;
}