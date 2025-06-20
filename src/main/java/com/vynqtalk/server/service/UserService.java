package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.dto.request.LoginRequest;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(LoginRequest loginRequest) {
        User dbUser = userRepository.findByEmail(loginRequest.getEmail());
        if (dbUser == null) {
            return false;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return (passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword()));
    }

    public User saveUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");
        user.setLastActive(Instant.now());
        user.setBio("No bio yet");
        user.setIsAdmin(false);
        return userRepository.save(user);
    }

    public User updateUser(UserDTO userDTO) {
        User user = this.getUserById(userDTO.id);
        user.setEmail(userDTO.email);
        user.setIsAdmin(userDTO.isAdmin);
        user.setName(userDTO.name);
        user.setStatus(userDTO.status);
        user.setLastActive(userDTO.lastActive);
        return userRepository.save(user);
    }

    // Get user by ID
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update user profile
    public User updateUser(Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            // Update fields as needed
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setAvatar(user.getAvatar());
            // Add other fields as necessary
            return userRepository.save(existingUser);
        }
        return null;
    }

    // Delete user account
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // List all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
