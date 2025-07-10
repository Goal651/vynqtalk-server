package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.request.UserCreateRequest;
import com.vynqtalk.server.dto.request.UserSettingsUpdateRequest;
import com.vynqtalk.server.dto.request.UserUpdateRequest;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.ExportUserDTO;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.dto.user.UserSettingsDTO;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.mapper.UserSettingsMapper;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSettingsService userSettingsService;
    private final UserSettingsMapper userSettingsMapper;

    public UserController(UserService userService, UserMapper userMapper, UserSettingsService userSettingsService,
            UserSettingsMapper userSettingsMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userSettingsService = userSettingsService;
        this.userSettingsMapper = userSettingsMapper;
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
        return ResponseEntity.ok(new ApiResponse<>(true,userMapper.toDTO(result), "User created successfully"));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true,userMapper.toDTO(user), "User retrieved successfully"));
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<ExportUserDTO>> getUserData(Principal principal) {
        ExportUserDTO userDTO = new ExportUserDTO();
        User user = userService.getUserByEmail(principal.getName());
        userDTO.setUser(userService.getUserWithUnreadMessages(user));
        return ResponseEntity.ok(new ApiResponse<>(true,userDTO, "Data processed successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserDTO>> getUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        user.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true,userMapper.toDTO(user), "Data processed successfully"));
    }

    // Update user profile
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateUser(Principal principal,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUserByEmail(principal.getName());
        user.setName(userUpdateRequest.getName());
        user.setBio(userUpdateRequest.getBio());
        userService.updateUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true,null,"User updated successfully"));
    }

    // Delete user account
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true,null, "User deleted successfully"));
    }

    // List all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(Principal principal) {
        List<UserDTO> userDTO = userService.getAllUsersWithLatestMessage();
        return ResponseEntity.ok(
                new ApiResponse<>(true,userDTO, userDTO.isEmpty() ? "No users found" : "Users retrieved successfully"));
    }

    // Get user settings
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> getUserSettings(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        UserSettings settings = userSettingsService.getUserSettings(user);
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(true,dto, "User settings retrieved successfully"));
    }

    // Update user settings
    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> updateUserSettings(Principal principal,
            @RequestBody UserSettingsUpdateRequest updatedSettings) {
        User user = userService.getUserByEmail(principal.getName());
        UserSettings settings = userSettingsService.updateUserSettings(user, updatedSettings);
        UserSettingsDTO dto = userSettingsMapper.toDTO(settings);
        return ResponseEntity.ok(new ApiResponse<>(true,dto, "User settings updated successfully"));
    }

}
