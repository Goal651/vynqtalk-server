package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.vynqtalk.server.dto.response.ApiResponse;

@RestController
@RequestMapping("/vapid")
public class VapidKeyController {
    @Value("${vapid.public}")
    private String vapidPublicKey;

    @GetMapping("/public-key")
    public ResponseEntity<ApiResponse<String>> getVapidPublicKey() {
        System.out.println("This is public vaid key"+ vapidPublicKey);
        return ResponseEntity.ok(new ApiResponse<>(true, vapidPublicKey, "VAPID public key retrieved successfully"));
    }
} 