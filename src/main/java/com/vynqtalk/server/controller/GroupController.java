package com.vynqtalk.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.service.GroupService;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // Get all groups
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupService.findById(id);
        return group != null ? ResponseEntity.ok(group) : ResponseEntity.notFound().build();
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group savedGroup = groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    // Delete group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
