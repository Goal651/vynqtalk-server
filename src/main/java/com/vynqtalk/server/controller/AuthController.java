package com.vynqtalk.server.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.dto.request.LoginRequest;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.response.AuthData;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.UserSettings;
import com.vynqtalk.server.service.JwtService;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserSettingsService userSettingsService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthData>> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(null, "Email and password are required", HttpStatus.BAD_REQUEST.value()));
        }
        User user = userService.getUserByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(null, "User not found", HttpStatus.UNAUTHORIZED.value()));
        }

        if (user.getStatus().equals("blocked")) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(null, "User is blocked contact admin", HttpStatus.UNAUTHORIZED.value()));
        }

        boolean authResult = userService.authenticate(loginRequest);

        if (!authResult) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(null, "Incorrect password", HttpStatus.UNAUTHORIZED.value()));
        }


        String token = jwtService.generateToken(user.getEmail());
        AuthData loginData = new AuthData(user, token);
        return ResponseEntity.ok(new ApiResponse<>(loginData, "Login successful", HttpStatus.OK.value()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthData>> signup(@RequestBody User user) {

        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            return ResponseEntity.ok().body(
                    new ApiResponse<>(null, "Email, password, and name are required", HttpStatus.BAD_REQUEST.value()));
        }
        user.setIsAdmin(false); // Default to non-admin user
        user.setCreatedAt(Instant.now());
        // Check if user already exists
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(null, "User already exists", HttpStatus.CONFLICT.value()));
        }
        userService.saveUser(user);
        userSettingsService.updateUserSettings(user, new UserSettings());
        String token = jwtService.generateToken(user.getEmail());
        AuthData authData = new AuthData(user, token);
        System.out.println("User create" + authData);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(authData, "Signup successful", HttpStatus.CREATED.value()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // Invalidate the JWT token logic can be implemented here
        return ResponseEntity.ok(new ApiResponse<>(null, "Logout successful", HttpStatus.OK.value()));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to handle forgot password 
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset link sent to email", HttpStatus.OK.value()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(null, "Email and new password are required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to reset the password can be implemented here
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(null, "Email and new password are required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to change the password can be implemented here
        return ResponseEntity.ok(new ApiResponse<>(null, "Password changed successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to verify the email can be implemented here
        return ResponseEntity.ok(new ApiResponse<>(null, "Email verification link sent", HttpStatus.OK.value()));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to resend the verification email can be implemented here
        return ResponseEntity.ok(new ApiResponse<>(null, "Verification email resent", HttpStatus.OK.value()));
    }

}
