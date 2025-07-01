package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.users.UserSettings;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    UserSettings findByUserId(Long userId);
} 