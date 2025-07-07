package com.vynqtalk.server.repository;

import java.util.List;
import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vynqtalk.server.model.messages.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.sender
                LEFT JOIN FETCH m.receiver
                LEFT JOIN FETCH m.replyTo r
                LEFT JOIN FETCH r.sender
                LEFT JOIN FETCH r.receiver
                WHERE (m.sender.id = :userA AND m.receiver.id = :userB)
                   OR (m.sender.id = :userB AND m.receiver.id = :userA)
                ORDER BY m.timestamp ASC
            """)
    List<Message> findChatBetweenUsers(@Param("userA") Long userA, @Param("userB") Long userB);

    long countByTimestampBetween(Instant start, Instant end);

    @Query("""
        SELECT m FROM Message m
        WHERE m.sender.id = :userId OR m.receiver.id = :userId
        ORDER BY m.timestamp DESC
    """)
    List<Message> findLatestMessageByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT m FROM Message m
        WHERE m.receiver.id = :userId AND m.isRead = false
        ORDER BY m.timestamp DESC
    """)
    List<Message> findUnreadMessagesByUserId(@Param("userId") Long userId);
}
