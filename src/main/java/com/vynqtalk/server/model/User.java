package com.vynqtalk.server.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String avatar;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin", nullable = true)
    private Boolean isAdmin;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant lastActive;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String bio;
}
