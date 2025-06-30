package com.vynqtalk.server.model;

import com.vynqtalk.server.model.enums.Theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "notification_enabled", nullable = false)
    private Boolean notificationEnabled = true;

    @Column(name = "email_notifications", nullable = false)
    private Boolean emailNotifications = true;

    @Column(name = "push_notifications", nullable = false)
    private Boolean pushNotifications = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false)
    private Theme theme = Theme.BLUE;

    @Column(name = "language", nullable = false)
    private String language = "en";

    @Column(name = "timezone", nullable = false)
    private String timezone = "UTC";

    @Column(nullable = false)
    private Boolean showOnlineStatus = true;

    @Column(nullable = false)
    private Boolean readReceipts = true;
}