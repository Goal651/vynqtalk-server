package com.vynqtalk.server.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_status")
public class SystemStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean inMaintenance;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Instant updatedAt;
}

// Note: Ensure all required fields are set before persisting. 