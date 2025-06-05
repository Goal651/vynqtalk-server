package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.UserDTO;
import com.vynqtalk.server.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    // Create a new user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        User result = userService.saveUser(user);
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(result), "User created successfully", 201));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>>  getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "User not found", 404));
        }
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(user), "User retrieved successfully", 200));
    }

    // Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "User not found", 404));
        }
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(updatedUser), "User updated successfully", 200));
    }

    // Delete user account
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "User deleted successfully", 200));
    }

    // List all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("This is wigo test " + users);
        if (users.isEmpty()) {
            return ResponseEntity.ok().body(new ApiResponse<>(null, "No users found", 404));
        }
        List<UserDTO> userDTO = users.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(userDTO, "Users retrieved successfully", 200));
    }
}
