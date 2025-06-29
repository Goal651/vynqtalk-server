package com.vynqtalk.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.service.GroupService;
import com.vynqtalk.server.service.UserService;

@RestController
@RequestMapping("/api/v1/group_member/{groupId}")
public class GroupMemberController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    // Get members
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getMembers(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }
        List<User> members = group.getMembers();
        List<UserDTO> userDTO = members.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(userDTO, "Members retrieved successfully", 200));
    }

    // Add member
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDTO>> addMember(@PathVariable Long groupId, @RequestBody User user) {
        User existingUser = userService.getUserById(user.getId());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "User not found", HttpStatus.NOT_FOUND.value()));
        }

        Group group = groupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }

        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member added successfully", 200));
    }

    // Change user role
    @PutMapping
    public ResponseEntity<ApiResponse<GroupDTO>> updateMember(@PathVariable Long groupId, @RequestBody User user) {
        User existingUser = userService.getUserById(user.getId());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "User not found", HttpStatus.NOT_FOUND.value()));
        }

        Group group = groupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }

        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member added successfully", 200));
    }

    // delete member
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<GroupDTO>> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        Group group = groupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "User not found", HttpStatus.NOT_FOUND.value()));
        }

        Group updatedGroup = groupService.removeMember(group, user);
        return ResponseEntity
                .ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member removed successfully", 200));
    }

}
