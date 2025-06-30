package com.vynqtalk.server.controller;

import java.util.List;

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
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.group.GroupMemberRequest;
import com.vynqtalk.server.error.UserNotFoundException;

@RestController
@RequestMapping("/api/v1/group_member/{groupId}")
public class GroupMemberController {

    private final UserService userService;
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    public GroupMemberController(UserService userService, GroupService groupService, GroupMapper groupMapper, UserMapper userMapper) {
        this.userService = userService;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    // Get members
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getMembers(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        List<User> members = group.getMembers();
        List<UserDTO> userDTO = members.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(userDTO, "Members retrieved successfully", 200));
    }

    // Add member
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDTO>> addMember(@PathVariable Long groupId, @Valid @RequestBody GroupMemberRequest request) {
        User existingUser = userService.getUserById(request.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Group group = groupService.findById(groupId);
        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member added successfully", 200));
    }

    // Change user role
    @PutMapping
    public ResponseEntity<ApiResponse<GroupDTO>> updateMember(@PathVariable Long groupId, @Valid @RequestBody GroupMemberRequest request) {
        User existingUser = userService.getUserById(request.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Group group = groupService.findById(groupId);
        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member added successfully", 200));
    }

    // delete member
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<GroupDTO>> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        Group group = groupService.findById(groupId);
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Group updatedGroup = groupService.removeMember(group, user);
        return ResponseEntity
                .ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member removed successfully", 200));
    }

}
