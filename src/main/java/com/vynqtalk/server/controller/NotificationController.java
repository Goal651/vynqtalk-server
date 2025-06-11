package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.notifications.NotificationDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.NotificationMapper;
import com.vynqtalk.server.model.Notification;
import com.vynqtalk.server.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> results = notificationService.getNotificationsByUserId(userId);
        List<NotificationDTO> notifications =results.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(notifications, "Notifications retrieved successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationDTO>> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id).orElse(null);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponse<>(notificationMapper.toDTO(notification), "Notification retrieved successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(new ApiResponse<>(notificationMapper.toDTO(savedNotification), "Notification created successfully", 201));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponse<>(notificationMapper.toDTO(notification), "Notification marked as read", 200));
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
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(notificationDTOs, "Notifications retrieved successfully", 200));
    }

} 