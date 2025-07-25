package com.vynqtalk.server.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;
import com.vynqtalk.server.repository.UserSettingsRepository;
import com.vynqtalk.server.dto.user.UserSettingsUpdateRequest;
import com.vynqtalk.server.exceptions.UserSettingsNotFoundException;

@Service
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    /**
     * Gets user settings for a user, creating defaults if not found.
     */
    public UserSettings getUserSettings(User user) {
        UserSettings settings = userSettingsRepository.findByUserId(user.getId());
        if (settings == null) {
            settings = createDefaultSettings(user);
        }
        return settings;
    }

    /**
     * Updates user settings for a user.
     * @throws UserSettingsNotFoundException if not found
     */
    @Transactional
    public UserSettings updateUserSettings(User user, UserSettingsUpdateRequest updatedSettings) {
        UserSettings existingSettings = userSettingsRepository.findByUserId(user.getId());
        if (existingSettings == null) {
            existingSettings = createDefaultSettings(user);
        }

        // Update only the fields that are not null
        if (updatedSettings.getNotificationEnabled() != null) {
            existingSettings.setNotificationEnabled(updatedSettings.getNotificationEnabled());
        }

        if (updatedSettings.getTheme() != null) {
            existingSettings.setTheme(updatedSettings.getTheme());
        }

        if (updatedSettings.getShowOnlineStatus() != null) {
            existingSettings.setShowOnlineStatus(updatedSettings.getShowOnlineStatus());
        }

        return userSettingsRepository.save(existingSettings);
    }

    /**
     * Creates default user settings for a user.
     */
    @Transactional
    private UserSettings createDefaultSettings(User user) {
        UserSettings settings = new UserSettings();
        settings.setUser(user);
        return userSettingsRepository.save(settings);
    }

    /**
     * Deletes user settings for a user.
     */
    @Transactional
    public void deleteUserSettings(User user) {
        UserSettings settings = userSettingsRepository.findByUserId(user.getId());
        if (settings != null) {
            userSettingsRepository.delete(settings);
        }
    }
} 