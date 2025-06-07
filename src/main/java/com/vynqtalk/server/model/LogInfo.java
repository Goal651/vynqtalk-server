package com.vynqtalk.server.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LogInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String log;

    @Column(nullable = false)
    private String logType;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String device;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String error;
    
}
