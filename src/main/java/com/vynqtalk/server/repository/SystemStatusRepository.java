package com.vynqtalk.server.repository;

import com.vynqtalk.server.model.SystemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemStatusRepository extends JpaRepository<SystemStatus, Long> {
} 