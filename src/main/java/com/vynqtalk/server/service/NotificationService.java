package com.vynqtalk.server.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;

import com.vynqtalk.server.exceptions.NotificationNotFoundException;
import com.vynqtalk.server.model.system.Notification;
import com.vynqtalk.server.model.users.DeviceToken;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.DeviceTokenRepository;
import com.vynqtalk.server.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    @Value("${vapid.public}")
    private String vapidPublicKey;

    @Value("${vapid.private}")
    private String vapidPrivateKey;

    @Value("${vapid.subject}")
    private String vapidSubject;

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

    public void registerDeviceToken(User user, String endpoint, String p256dh, String auth) {
        deviceTokenRepository.findByEndpoint(endpoint).ifPresentOrElse(
                existing -> {},
                () -> deviceTokenRepository.save(new DeviceToken(user, endpoint, p256dh, auth)));
    }

    public void unregisterDeviceToken(String endpoint) {
        deviceTokenRepository.deleteByEndpoint(endpoint);
    }

    /**
     * Sends a push notification to all device tokens of the user.
     * @param user The user to notify
     * @param title The notification title
     * @param body The notification body
     */
    public void sendPushNotificationToUser(User user, String title, String body) {
        List<DeviceToken> tokens = deviceTokenRepository.findByUser(user);

        PushService pushService;
        try {
            pushService = new PushService()
                .setPublicKey(vapidPublicKey)
                .setPrivateKey(vapidPrivateKey)
                .setSubject(vapidSubject);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            logger.severe("Failed to initialize PushService: " + e.getMessage());
            return;
        }

        String payloadJson = String.format(
            "{ \"title\": \"%s\", \"body\": \"%s\", \"icon\": \"/logo.svg\", \"badge\": \"/logo.svg\", \"data\": { \"url\": \"/some-path\" } }",
            title, body
        );

        for (DeviceToken deviceToken : tokens) {
            try {
                nl.martijndwars.webpush.Notification notification = new nl.martijndwars.webpush.Notification(
                    deviceToken.getEndpoint(),
                    deviceToken.getP256dh(),
                    deviceToken.getAuth(),
                    payloadJson.getBytes()
                );
                HttpResponse response = pushService.send(notification);
                int statusCode = response.getStatusLine().getStatusCode();
                logger.info("Push sent to " + deviceToken.getEndpoint() + " with response: " + response.getStatusLine());

                // Remove expired/invalid subscriptions
                if (statusCode == 410 || statusCode == 404) {
                    logger.info("Removing expired push subscription: " + deviceToken.getEndpoint());
                    deviceTokenRepository.deleteByEndpoint(deviceToken.getEndpoint());
                }
            } catch (Exception e) {
                logger.warning("Failed to send push: " + e.getMessage());
            }
        }
    }
}
