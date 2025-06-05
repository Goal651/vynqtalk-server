package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.UserSettings;
import com.vynqtalk.server.repository.UserSettingsRepository;

@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    public UserSettings getUserSettings(User user) {
        UserSettings settings = userSettingsRepository.findByUserId(user.getId());
        if (settings == null) {
            // Create default settings if none exist
            settings = createDefaultSettings(user);
        }
        return settings;
    }

    public UserSettings updateUserSettings(User user, UserSettings updatedSettings) {
        UserSettings existingSettings = userSettingsRepository.findByUserId(user.getId());
        if (existingSettings == null) {
            existingSettings = createDefaultSettings(user);
        }

        // Update only the fields that are not null
        if (updatedSettings.getNotificationEnabled() != null) {
            existingSettings.setNotificationEnabled(updatedSettings.getNotificationEnabled());
        }
        if (updatedSettings.getEmailNotifications() != null) {
            existingSettings.setEmailNotifications(updatedSettings.getEmailNotifications());
        }
        if (updatedSettings.getPushNotifications() != null) {
            existingSettings.setPushNotifications(updatedSettings.getPushNotifications());
        }
        if (updatedSettings.getTheme() != null) {
            existingSettings.setTheme(updatedSettings.getTheme());
        }
        if (updatedSettings.getLanguage() != null) {
            existingSettings.setLanguage(updatedSettings.getLanguage());
        }
        if (updatedSettings.getTimezone() != null) {
            existingSettings.setTimezone(updatedSettings.getTimezone());
        }

        if (updatedSettings.getShowOnlineStatus() != null) {
            existingSettings.setShowOnlineStatus(updatedSettings.getShowOnlineStatus());
        }
        if (updatedSettings.getReadReceipts() != null) {
            existingSettings.setReadReceipts(updatedSettings.getReadReceipts());
        }

        return userSettingsRepository.save(existingSettings);
    }

    private UserSettings createDefaultSettings(User user) {
        UserSettings settings = new UserSettings();
        settings.setUser(user);
        return userSettingsRepository.save(settings);
    }

    public void deleteUserSettings(User user) {
        UserSettings settings = userSettingsRepository.findByUserId(user.getId());
        if (settings != null) {
            userSettingsRepository.delete(settings);
        }
    }
} 