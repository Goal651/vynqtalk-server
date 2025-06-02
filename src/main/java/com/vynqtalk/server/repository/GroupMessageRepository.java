package com.vynqtalk.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.vynqtalk.server.model.GroupMessage;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
    // Additional query methods can be defined here if needed
    List<GroupMessage> findByGroupId(@Param("groupId") Long groupId);
}
