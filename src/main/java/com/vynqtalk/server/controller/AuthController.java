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
import com.vynqtalk.server.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User user) {
        // Authenticate the user
        if (!userService.authenticate(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, "Invalid credentials", HttpStatus.UNAUTHORIZED.value()));
        }
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(token, "Login successful", HttpStatus.OK.value()));
    }

}
