package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.system.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
    List<Notification> findByType(String type);
}
