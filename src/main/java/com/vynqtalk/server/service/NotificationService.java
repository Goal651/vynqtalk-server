package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.repository.NotificationRepository;
import java.util.List;
import java.util.Optional;
import com.vynqtalk.server.error.NotificationNotFoundException;
import com.vynqtalk.server.model.system.Notification;

@Service
public class NotificationService {
    
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Gets notifications by user ID.
     */
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    /**
     * Gets a notification by ID.
     * @throws NotificationNotFoundException if not found
     */
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
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
     * @throws NotificationNotFoundException if not found
     */
    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
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
}
