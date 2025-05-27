package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.AuthResult;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public AuthResult authenticate(User user) {
        User dbUser = userRepo.findByEmail(user.getEmail());
        return (dbUser != null && user.getPassword() == dbUser.getPassword())
                ? new AuthResult(true, dbUser)
                : new AuthResult(false, null);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    // Get user by ID
    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    // Update user profile
    public User updateUser(Long id, User user) {
        Optional<User> existingUserOpt = userRepo.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            // Update fields as needed
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            // Add other fields as necessary
            return userRepo.save(existingUser);
        }
        return null;
    }

    // Delete user account
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    // List all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
