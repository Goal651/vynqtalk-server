package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.model.ApiResponse;
import com.vynqtalk.server.model.AuthResult;
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
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(null, "Email and password are required", HttpStatus.BAD_REQUEST.value()));
        }
        AuthResult authResult = userService.authenticate(user);
        if (!authResult.isAuth()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value()));
        }
        User authenticatedUser = authResult.getUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value()));
        }
        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(token, "Login successful", HttpStatus.OK.value()));
    }

}
