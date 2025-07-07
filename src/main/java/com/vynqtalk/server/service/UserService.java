package com.vynqtalk.server.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.dto.request.LoginRequest;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.repository.UserRepository;
import com.vynqtalk.server.model.enums.AlertType;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.error.UserNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for user-related operations, including authentication, user
 * management, and login attempt tracking.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AlertService alertService;
    private final Map<String, Integer> failedLoginAttempts = new ConcurrentHashMap<>();

    public UserService(UserRepository userRepository, AlertService alertService) {
        this.userRepository = userRepository;
        this.alertService = alertService;
    }

    /**
     * Authenticates a user by email and password, tracking failed attempts by IP.
     */
    public boolean authenticate(LoginRequest loginRequest, String ipAddress) {
        User dbUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean success = dbUser != null && passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword());

        if (!success) {
            String key = ipAddress;
            int attempts = failedLoginAttempts.getOrDefault(key, 0) + 1;
            failedLoginAttempts.put(key, attempts);

            if (attempts >= 3) {
                alertService.logAlert(AlertType.WARNING, "3+ failed login attempts for " + loginRequest.getEmail(), ipAddress,loginRequest.getEmail());
                failedLoginAttempts.remove(key);
            }
        } else {
            failedLoginAttempts.remove(ipAddress);
        }
        return success;
    }

    /**
     * Saves a new user, encoding the password and setting default fields.
     */
    @Transactional
    public User saveUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");
        user.setLastActive(Instant.now());
        user.setBio("No bio yet");
        user.setUserRole(user.getUserRole());;
        return userRepository.save(user);
    }

    /**
     * Updates a user using a UserDTO.
     */
    @Transactional
    public User updateUser(UserDTO userDTO) {
        User user = this.getUserById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userDTO.getId()));
        user.setEmail(userDTO.getEmail());
        user.setUserRole(userDTO.getUserRole());
        user.setName(userDTO.getName());
        user.setStatus(userDTO.getStatus());
        user.setLastActive(userDTO.getLastActive());
        return userRepository.save(user);
    }

    /**
     * Gets a user by ID.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Gets a user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates a user by ID using a User entity.
     */
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getEmail()));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAvatar(user.getAvatar());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Returns all users.
     */
    public List<User> getAllUsers() {
        List<User> response= userRepository.findAll();
        response.forEach((User user)->{
            user.setPassword(null);
        });
        return response;
    }
}
