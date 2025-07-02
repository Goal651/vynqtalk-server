package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.UserService;
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.user.UserCreateRequest;
import com.vynqtalk.server.error.UserNotFoundException;
import com.vynqtalk.server.dto.user.UserUpdateRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreateRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setPassword(userRequest.getPassword());
        user.setUserRole(UserRole.USER);
        User result = userService.saveUser(user);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(userMapper.toDTO(result), "User created successfully", 201));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(user), "User retrieved successfully", 200));
    }

    // Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setEmail(userUpdateRequest.getEmail());
        user.setUserRole(userUpdateRequest.getUserRole());
        user.setName(userUpdateRequest.getName());
        user.setStatus(userUpdateRequest.getStatus());
        user.setLastActive(userUpdateRequest.getLastActive());
        // Do not set password here; handle in service if needed
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(new ApiResponse<>(userMapper.toDTO(updatedUser), "User updated successfully", 200));
    }

    // Delete user account
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "User deleted successfully", 200));
    }

    // List all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTO = users.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(
                new ApiResponse<>(userDTO, userDTO.isEmpty() ? "No users found" : "Users retrieved successfully", 200));
    }
}
