package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.notifications.NotificationDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.NotificationMapper;
import com.vynqtalk.server.model.system.Notification;
import com.vynqtalk.server.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.notifications.NotificationCreateRequest;
import com.vynqtalk.server.error.NotificationNotFoundException;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> results = notificationService.getNotificationsByUserId(userId);
        List<NotificationDTO> notifications = results.stream()
                .map(notificationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(notifications, "Notifications retrieved successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationDTO>> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        return ResponseEntity.ok(
                new ApiResponse<>(notificationMapper.toDTO(notification), "Notification retrieved successfully", 200));
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
        return ResponseEntity.status(201).body(new ApiResponse<>(notificationMapper.toDTO(savedNotification),
                "Notification created successfully", 201));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification == null) {
            throw new NotificationNotFoundException("Notification not found with id: " + id);
        }
        return ResponseEntity
                .ok(new ApiResponse<>(notificationMapper.toDTO(notification), "Notification marked as read", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Notification deleted successfully", 200));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByType(@PathVariable String type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notificationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(notificationDTOs, "Notifications retrieved successfully", 200));
    }

    @PostMapping("/device/register")
    public ResponseEntity<ApiResponse<Void>> registerDeviceToken(Principal principal, @RequestParam String token) {
        notificationService.registerDeviceToken(
                // You may want to use userService.getUserByEmail(principal.getName()).get() in
                // real code
                new com.vynqtalk.server.model.users.User() {
                    {
                        setEmail(principal.getName());
                    }
                },
                token);
        return ResponseEntity.ok(new ApiResponse<>(null, "Device token registered", 200));
    }

    @PostMapping("/device/unregister")
    public ResponseEntity<ApiResponse<Void>> unregisterDeviceToken(@RequestParam String token) {
        notificationService.unregisterDeviceToken(token);
        return ResponseEntity.ok(new ApiResponse<>(null, "Device token unregistered", 200));
    }

}