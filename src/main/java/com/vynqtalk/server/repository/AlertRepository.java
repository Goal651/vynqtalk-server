package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.system.Alert;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Custom queries can be added here
    List<Alert> findAllByOrderByCreatedAtDesc(Pageable pageable);
}