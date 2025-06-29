package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.Group;
import java.time.Instant;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // Custom query methods can be added here if needed
    // For example, to find groups by name or members, etc.

    long countByCreatedAtAfter(Instant instant);
}
