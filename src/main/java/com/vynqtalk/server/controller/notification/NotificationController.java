package com.vynqtalk.server.controller.notification;

import com.vynqtalk.server.dto.notifications.NotificationDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.NotificationMapper;
import com.vynqtalk.server.model.system.Notification;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.notification.NotificationService;
import com.vynqtalk.server.service.user.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.notifications.NotificationCreateRequest;
import com.vynqtalk.server.dto.notifications.PushSubscriptionDTO;

import java.security.Principal;

import java.util.List;



@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper,
            UserService userService) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByUserId(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Notification> results = notificationService.getNotificationsByUserId(user.getId());
        List<NotificationDTO> notifications = notificationMapper.toDTOs(results);
        return ResponseEntity.ok(new ApiResponse<>(true, notifications, "Notifications retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationDTO>> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, notificationMapper.toDTO(notification), "Notification retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> createNotification(
            @Valid @RequestBody NotificationCreateRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setTitle(notificationRequest.getTitle());
        notification.setMessage(notificationRequest.getMessage());
        notification.setType(notificationRequest.getType());
        notification.setIsRead(false);
        notification.setTimestamp(java.time.Instant.now());
        // Set user if needed (userService.getUserById(notificationRequest.getUserId()))
        Notification savedNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(new ApiResponse<>(true, notificationMapper.toDTO(savedNotification),
                "Notification created successfully"));
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity
                .ok(new ApiResponse<>(true, notificationMapper.toDTO(notification), "Notification marked as read"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Notification deleted successfully"));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByType(@PathVariable String type) {
        List<Notification> result = notificationService.getNotificationsByType(type);
        List<NotificationDTO> notifications = notificationMapper.toDTOs(result);
        return ResponseEntity.ok(new ApiResponse<>(true, notifications, "Notifications retrieved successfully"));
    }



    @PostMapping("/device/register")
    public ResponseEntity<ApiResponse<Void>> registerDeviceToken(Principal principal, @RequestBody PushSubscriptionDTO subscription) {
        System.out.println("Subscription logging"+subscription);
        notificationService.registerDeviceToken(
                userService.getUserByEmail(principal.getName()),
                subscription.getEndpoint(),
                subscription.getKeys().getP256dh(),
                subscription.getKeys().getAuth());
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Device token registered"));
    }

    @PostMapping("/device/unregister")
    public ResponseEntity<ApiResponse<Void>> unregisterDeviceToken(@RequestParam String endpoint) {
        notificationService.unregisterDeviceToken(endpoint);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Device token unregistered"));
    }
}