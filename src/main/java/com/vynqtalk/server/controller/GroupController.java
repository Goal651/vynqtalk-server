package com.vynqtalk.server.controller;

import java.security.Principal;
import java.time.Instant;
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

import com.vynqtalk.server.dto.GroupDTO;
import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.service.GroupService;
import com.vynqtalk.server.service.UserService;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupMapper groupMapper;

    // Get all groups
    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups(Principal principal) {
        List<Group> groups = groupService.findAll();

        if (principal != null) {
            User currentUser = userService.getUserByEmail(principal.getName());
            // Filter groups where the user is a member
            groups = groups.stream()
                    .filter(g -> g.getMembers().stream().anyMatch(m -> m.getId().equals(currentUser.getId())))
                    .toList();
        }

        if (groups.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(List.of(), "No groups found", 200));
        }

        List<GroupDTO> groupDTO = groups.stream()
                .map(groupMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(groupDTO, "Groups retrieved successfully", 200));
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDTO>> getGroupById(@PathVariable Long id) {
        Group group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(group), "Group retrieved successfully", 200));
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDTO>> createGroup(Principal principal, @RequestBody Group group) {
        User createdBy = userService.getUserByEmail(principal.getName());
        if (createdBy == null) {
            return ResponseEntity.ok(new ApiResponse<>(null, "User not found", HttpStatus.UNAUTHORIZED.value()));
        }
        group.setMembers(List.of(createdBy)); 
        group.setCreatedBy(createdBy);
        group.setCreatedAt(Instant.now());
        group.setIsPrivate(false);
        group.setStatus("active");
        Group savedGroup = groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(groupMapper.toDTO(savedGroup), "Group created successfully",
                        HttpStatus.CREATED.value()));
    }

    // Update an existing group
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDTO>> updateGroup(@PathVariable Long id, @RequestBody Group updatedGroup) {
        System.out.println("Updating group with ID: " + id);
        if (updatedGroup.getName() == null || updatedGroup.getDescription() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Group name and description cannot be null", HttpStatus.BAD_REQUEST.value()));
        }
        Group existingGroup = groupService.findById(id);
        if (existingGroup == null) {
            return ResponseEntity.notFound().build();
        }
        updatedGroup.setId(id);
        Group savedGroup = groupService.save(updatedGroup);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(savedGroup), "Group updated successfully", 200));
    }

    // Add member
    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<GroupDTO>> addMember(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "User not found", HttpStatus.NOT_FOUND.value()));
        }

        Group group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }
        
        Group updatedGroup = groupService.addMember(group, existingUser);
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member added successfully", 200));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<ApiResponse<GroupDTO>> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        Group group = groupService.findById(id);
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
        return ResponseEntity.ok(new ApiResponse<>(groupMapper.toDTO(updatedGroup), "Member removed successfully", 200));
    }

    // Delete group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Group deleted successfully", 200));
    }
}
