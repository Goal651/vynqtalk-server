package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.model.ApiResponse;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.service.JwtService;

@RestController
@RequestMapping("/api/login")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User user) {
        String token = jwtService.generateToken(1L);
        return ResponseEntity.ok(new ApiResponse<>(token, "Login successful", HttpStatus.OK.value()));

    }

}
