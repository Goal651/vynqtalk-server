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
import com.vynqtalk.server.dto.request.MemberRequest;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.GroupService;
import com.vynqtalk.server.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/member/{groupId}")
public class MemberController {

    private final UserService userService;
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    public MemberController(UserService userService, GroupService groupService, GroupMapper groupMapper,
            UserMapper userMapper) {
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
        List<UserDTO> userDTO = userMapper.toDTOs(members);
        return ResponseEntity.ok(new ApiResponse<>(true, userDTO, "Members retrieved successfully"));
    }

    // Add member
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<GroupDTO>> addMember(@PathVariable Long groupId,
            @PathVariable Long userId) {
        User existingUser = userService.getUserById(userId);
        Group group = groupService.findById(groupId);
        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(true, groupMapper.toDTO(updatedGroup), "Member added successfully"));
    }

    // Change user role
    @PutMapping
    public ResponseEntity<ApiResponse<GroupDTO>> updateMember(@PathVariable Long groupId,
            @Valid @RequestBody MemberRequest request) {
        User existingUser = userService.getUserById(request.getUserId());
        Group group = groupService.findById(groupId);
        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(true, groupMapper.toDTO(updatedGroup), "Member added successfully"));
    }

    // delete member
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<GroupDTO>> removeMember(@PathVariable Long groupId,
            @PathVariable Long memberId) {
        Group group = groupService.findById(groupId);
        User user = userService.getUserById(memberId);
        Group updatedGroup = groupService.removeMember(group, user);
        return ResponseEntity
                .ok(new ApiResponse<>(true, groupMapper.toDTO(updatedGroup), "Member removed successfully"));
    }

}
