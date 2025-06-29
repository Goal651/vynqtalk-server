package com.vynqtalk.server.repository;

import java.util.List;
import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vynqtalk.server.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.sender
                LEFT JOIN FETCH m.receiver
                LEFT JOIN FETCH m.replyToMessage r
                LEFT JOIN FETCH r.sender
                LEFT JOIN FETCH r.receiver
                WHERE (m.sender.id = :userA AND m.receiver.id = :userB)
                   OR (m.sender.id = :userB AND m.receiver.id = :userA)
                ORDER BY m.timestamp ASC
            """)
    List<Message> findChatBetweenUsers(@Param("userA") Long userA, @Param("userB") Long userB);

    long countByTimestampBetween(Instant start, Instant end);
}
