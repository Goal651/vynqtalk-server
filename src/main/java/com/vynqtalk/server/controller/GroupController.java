package com.vynqtalk.server.controller;

import java.security.Principal;
import java.time.Instant;
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
import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.GroupService;
import com.vynqtalk.server.service.UserService;
import jakarta.validation.Valid;

import com.vynqtalk.server.dto.request.GroupCreateRequest;
import com.vynqtalk.server.dto.request.GroupUpdateRequest;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, UserService userService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
    }

    // Get all groups
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups(Principal principal) {
        List<Group> groups = groupService.findAll();
        if (principal != null) {
            User currentUser = userService.getUserByEmail(principal.getName());

            groups = groups.stream()
                    .filter(g -> g.getMembers().stream().anyMatch(m -> m.getId().equals(currentUser.getId())))
                    .toList();
        }

        List<GroupDTO> groupDTO = groupMapper.toDTOs(groups);
        return ResponseEntity.ok(new ApiResponse<>(true, groupDTO,
                groupDTO.isEmpty() ? "No groups found" : "Groups retrieved successfully"));
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDTO>> getGroupById(@PathVariable Long id) {
        Group group = groupService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, groupMapper.toDTO(group), "Group retrieved successfully"));
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDTO>> createGroup(Principal principal,
            @Valid @RequestBody GroupCreateRequest groupRequest) {
        User createdBy = userService.getUserByEmail(principal.getName());
        Group group = new Group();
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setAdmins(List.of(createdBy));
        group.setMembers(List.of(createdBy));
        group.setCreatedBy(createdBy);
        group.setCreatedAt(Instant.now());
        group.setIsPrivate(groupRequest.getIsPrivate() != null ? groupRequest.getIsPrivate() : false);
        group.setStatus("active");
        Group savedGroup = groupService.save(group);
        return ResponseEntity.ok(new ApiResponse<>(true, groupMapper.toDTO(savedGroup), "Group created successfully"));
    }

    // Update an existing group
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDTO>> updateGroup(@PathVariable Long id,
            @Valid @RequestBody GroupUpdateRequest groupRequest) {
        Group existingGroup = groupService.findById(id);
        existingGroup.setName(groupRequest.getName());
        existingGroup.setDescription(groupRequest.getDescription());
        if (groupRequest.getIsPrivate() != null) {
            existingGroup.setIsPrivate(groupRequest.getIsPrivate());
        }
        Group savedGroup = groupService.save(existingGroup);
        return ResponseEntity.ok(new ApiResponse<>(true,groupMapper.toDTO(savedGroup), "Group updated successfully"));
    }

    // Delete group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true,null, "Group deleted successfully"));
    }
}
