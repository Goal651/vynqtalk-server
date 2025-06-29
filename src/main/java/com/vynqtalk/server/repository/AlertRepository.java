package com.vynqtalk.server.repository;

import com.vynqtalk.server.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Custom queries can be added here
    List<Alert> findAllByOrderByCreatedAtDesc(Pageable pageable);
}