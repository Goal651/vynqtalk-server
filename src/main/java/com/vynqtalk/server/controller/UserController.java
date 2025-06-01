package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User result = userService.saveUser(user);
        return ResponseEntity.ok(new ApiResponse<>(result, "User created successfully", 201));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>>  getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "User not found", 404));
        }
        return ResponseEntity.ok(new ApiResponse<>(user, "User retrieved successfully", 200));
    }

    // Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "User not found", 404));
        }
        return ResponseEntity.ok(new ApiResponse<>(updatedUser, "User updated successfully", 200));
    }

    // Delete user account
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "User deleted successfully", 200));
    }

    // List all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "No users found", 404));
        }
        return ResponseEntity.ok(new ApiResponse<>(users, "Users retrieved successfully", 200));
    }
}
