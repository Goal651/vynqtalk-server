package com.vynqtalk.server.service.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.dto.auth.LoginRequest;
import com.vynqtalk.server.exceptions.UserNotFoundException;
import com.vynqtalk.server.model.enums.AlertType;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.UserRepository;
import com.vynqtalk.server.service.admin.AlertService;

@Service
public class AuthService {
    private final Map<String, Integer> failedLoginAttempts = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final AlertService alertService;

    public AuthService(UserRepository userRepository, AlertService alertService) {
        this.userRepository = userRepository;
        this.alertService = alertService;
    }

    /**
     * Authenticates a user by email and password, tracking failed attempts by IP.
     */
    public boolean authenticate(LoginRequest loginRequest, String ipAddress) {
        User dbUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean success = dbUser != null && passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword());

        if (!success) {
            String key = ipAddress;
            int attempts = failedLoginAttempts.getOrDefault(key, 0) + 1;
            failedLoginAttempts.put(key, attempts);

            if (attempts >= 3) {
                alertService.logAlert(AlertType.WARNING, "3+ failed login attempts for " + loginRequest.getEmail(),
                        ipAddress, loginRequest.getEmail());
                failedLoginAttempts.remove(key);
            }
        } else {
            failedLoginAttempts.remove(ipAddress);
        }
        return success;
    }
}
