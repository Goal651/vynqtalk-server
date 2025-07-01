package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.system.SystemStatus;

public interface SystemStatusRepository extends JpaRepository<SystemStatus, Long> {
} 