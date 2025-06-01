package com.vynqtalk.server.controller;

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

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.service.GroupService;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // Get all groups
    @GetMapping
    public ResponseEntity<ApiResponse<List<Group>>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(groups, "Groups retrieved successfully", 200));
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Group>> getGroupById(@PathVariable Long id) {
        Group group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "Group not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(new ApiResponse<>(group, "Group retrieved successfully", 200));
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<ApiResponse<Group>> createGroup(@RequestBody Group group) {
        group.setCreatedAt(Instant.now());
        group.setCreatedBy("system");
        Group savedGroup = groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(savedGroup, "Group created successfully", HttpStatus.CREATED.value()));
    }

    // Update an existing group
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Group>> updateGroup(@PathVariable Long id, @RequestBody Group updatedGroup) {
        Group existingGroup = groupService.findById(id);
        if (existingGroup == null) {
            return ResponseEntity.notFound().build();
        }
        updatedGroup.setId(id);
        Group savedGroup = groupService.save(updatedGroup);
        return ResponseEntity.ok(new ApiResponse<>(savedGroup, "Group updated successfully", 200));
    }

    // Delete group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Group deleted successfully", 200));
    }

}
