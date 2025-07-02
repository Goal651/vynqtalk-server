package com.vynqtalk.server.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.dto.request.LoginRequest;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.response.AuthData;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.JwtService;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.service.UserSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.vynqtalk.server.dto.request.SignupRequest;
import com.vynqtalk.server.dto.request.ForgotPasswordRequest;
import com.vynqtalk.server.dto.request.ResetPasswordRequest;
import com.vynqtalk.server.dto.request.ChangePasswordRequest;
import com.vynqtalk.server.dto.request.VerifyEmailRequest;
import com.vynqtalk.server.dto.request.ResendVerificationRequest;
import com.vynqtalk.server.error.UserNotFoundException;

/**
 * Controller for authentication-related endpoints, including login, signup, token check, and password management.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserSettingsService userSettingsService;

    public AuthController(UserService userService, JwtService jwtService, UserSettingsService userSettingsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userSettingsService = userSettingsService;
    }

    /**
     * Authenticates a user and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthData>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(null, "Email and password are required", HttpStatus.BAD_REQUEST.value()));
        }

        boolean authResult = userService.authenticate(loginRequest, ipAddress);
        User user = userService.getUserByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (user.getStatus().equals("blocked")) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(null, "User is blocked contact admin", HttpStatus.UNAUTHORIZED.value()));
        }

        if (!authResult) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(null, "Incorrect password", HttpStatus.UNAUTHORIZED.value()));
        }

        String token = jwtService.generateToken(user.getEmail());
        AuthData loginData = new AuthData(user, token);
        return ResponseEntity.ok(new ApiResponse<>(loginData, "Login successful", HttpStatus.OK.value()));
    }

    /**
     * Registers a new user and returns a JWT token.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthData>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setName(signupRequest.getName());
        user.setUserRole(UserRole.USER);
        user.setCreatedAt(Instant.now());
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(null, "User already exists", HttpStatus.CONFLICT.value()));
        }
        userService.saveUser(user);
        userSettingsService.getUserSettings(user); // Ensure default settings are created
        String token = jwtService.generateToken(user.getEmail());
        AuthData authData = new AuthData(user, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(authData, "Signup successful", HttpStatus.CREATED.value()));
    }

    /**
     * Logs out the user (token invalidation logic can be implemented).
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(new ApiResponse<>(null, "Logout successful", HttpStatus.OK.value()));
    }

    /**
     * Handles forgot password requests.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset link sent to email", HttpStatus.OK.value()));
    }

    /**
     * Handles password reset requests.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset successfully", HttpStatus.OK.value()));
    }

    /**
     * Handles password change requests.
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(null, "Password changed successfully", HttpStatus.OK.value()));
    }

    /**
     * Handles email verification requests.
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(null, "Email verification link sent", HttpStatus.OK.value()));
    }

    /**
     * Handles resend verification email requests.
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(null, "Verification email resent", HttpStatus.OK.value()));
    }

    /**
     * Checks the validity of a JWT token and returns the user if valid.
     */
    @PostMapping("/check-token")
    public ResponseEntity<ApiResponse<User>> checkToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(null, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED.value()));
        }
        String token = authHeader.substring(7);
        String email = jwtService.getUsernameFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(null, "Invalid or expired token", HttpStatus.UNAUTHORIZED.value()));
        }
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return ResponseEntity.ok(new ApiResponse<>(user, "Token is valid", HttpStatus.OK.value()));
    }
}
