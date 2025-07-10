package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.groups.Group;

import java.time.Instant;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // Custom query methods can be added here if needed
    // For example, to find groups by name or members, etc.

    long countByCreatedAtAfter(Instant instant);
    // Find all groups where the user is an admin
    List<Group> findByAdmins_Id(Long userId);
    // Find all groups where the user is a member
   List<Group> findByMembers_Id(Long userId);
}
