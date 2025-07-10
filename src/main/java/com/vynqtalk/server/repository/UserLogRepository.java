package com.vynqtalk.server.repository;

import com.vynqtalk.server.model.users.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
} 