package com.vynqtalk.server.model.system;

import java.time.Instant;

import com.vynqtalk.server.model.enums.AlertType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String email;

    public Alert() {
    }

    public Alert(AlertType type, String message, String ipAddress, String email) {
        this.type = type;
        this.message = message;
        this.createdAt = Instant.now();
        this.ipAddress = ipAddress;
        this.email = email;
    }
}

// Note: Ensure all required fields are set before persisting.