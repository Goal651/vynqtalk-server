package com.vynqtalk.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vynqtalk.server.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userA AND m.receiver.id = :userB) OR " +
            "(m.sender.id = :userB AND m.receiver.id = :userA) ")
    List<Message> findChatBetweenUsers(@Param("userA") String userA, @Param("userB") String userB);

}
