package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vynqtalk.server.model.users.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT DATE(u.createdAt) as date, COUNT(u.id) as newUsers FROM User u GROUP BY DATE(u.createdAt)")
    List<Object[]> countNewUsersPerDay();

    @Query("SELECT DATE(u.lastActive) as date, COUNT(u.id) as activeUsers FROM User u WHERE u.lastActive >= :start AND u.lastActive < :end GROUP BY DATE(u.lastActive)")
    List<Object[]> countActiveUsersPerDay(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT DATE(m.timestamp) as date, COUNT(m.id) as messages FROM Message m GROUP BY DATE(m.timestamp)")
    List<Object[]> countMessagesPerDay();

    long countByCreatedAtAfter(Instant instant);
}
