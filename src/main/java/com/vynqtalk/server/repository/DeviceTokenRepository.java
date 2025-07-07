package com.vynqtalk.server.repository;

import com.vynqtalk.server.model.users.DeviceToken;
import com.vynqtalk.server.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByToken(String token);
    List<DeviceToken> findByUser(User user);
    void deleteByToken(String token);
} 