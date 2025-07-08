package com.vynqtalk.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.vynqtalk.server.error.NotificationNotFoundException;
import com.vynqtalk.server.model.system.Notification;
import com.vynqtalk.server.model.users.DeviceToken;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.DeviceTokenRepository;
import com.vynqtalk.server.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    public NotificationService(NotificationRepository notificationRepository,
            DeviceTokenRepository deviceTokenRepository) {
        this.notificationRepository = notificationRepository;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    /**
     * Gets notifications by user ID.
     */
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    /**
     * Gets a notification by ID.
     * 
     * @throws NotificationNotFoundException if not found
     */
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException());
    }

    /**
     * Creates a notification.
     */
    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Marks a notification as read by ID.
     * 
     * @throws NotificationNotFoundException if not found
     */
    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException());
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * Deletes a notification by ID.
     */
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    /**
     * Gets notifications by type.
     */
    public List<Notification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public void registerDeviceToken(User user, String token) {
        deviceTokenRepository.findByToken(token).ifPresentOrElse(
                existing -> {
                },
                () -> deviceTokenRepository.save(new DeviceToken(user, token)));
    }

    public void unregisterDeviceToken(String token) {
        deviceTokenRepository.deleteByToken(token);
    }
}
