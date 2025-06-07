package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.dto.GroupDTO;
import com.vynqtalk.server.dto.UserDTO;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.service.AdminService;
import com.vynqtalk.server.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(adminService.getAllUsers(), "Users fetched successfully", 200));
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups() {
        return ResponseEntity
                .ok(new ApiResponse<List<GroupDTO>>(adminService.getAllGroups(), "Groups fetched successfully", 200));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user=userService.getUserById(id);
        System.out.println("User status:             "+user.getStatus()+"User new status: "+userDTO.status);

        user.setEmail(userDTO.email);
        user.setIsAdmin(userDTO.isAdmin);
        user.setName(userDTO.name);
        user.setStatus(userDTO.status);
        user.setLastActive(userDTO.lastActive);
        User response=userService.updateUser(user);
        return ResponseEntity
                .ok(new ApiResponse<>(userMapper.toDTO(response), "User updated successfully", 200));
    }

}
