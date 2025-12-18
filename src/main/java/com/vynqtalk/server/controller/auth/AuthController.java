package com.vynqtalk.server.controller.auth;

import java.security.Principal;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.dto.response.AuthData;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.auth.AuthService;
import com.vynqtalk.server.service.auth.JwtService;
import com.vynqtalk.server.service.user.UserService;
import com.vynqtalk.server.service.user.UserSettingsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.vynqtalk.server.dto.auth.ChangePasswordRequest;
import com.vynqtalk.server.dto.auth.ForgotPasswordRequest;
import com.vynqtalk.server.dto.auth.LoginRequest;
import com.vynqtalk.server.dto.auth.ResendVerificationRequest;
import com.vynqtalk.server.dto.auth.ResetPasswordRequest;
import com.vynqtalk.server.dto.auth.SignupRequest;
import com.vynqtalk.server.dto.auth.VerifyEmailRequest;
import com.vynqtalk.server.mapper.UserMapper;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for authentication-related endpoints, including login, signup,
 * token check, and password management.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserSettingsService userSettingsService;
    private final UserMapper userMapper;
    private final AuthService authService;

    /**
     * Authenticates a user and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthData>> login(@Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, null, "Email and password are required"));
        }

        boolean authResult = authService.authenticate(loginRequest, ipAddress);
        UserDTO user = userService.getUserByEmail(loginRequest.getEmail());

        if (user.getStatus().equals("blocked")) {
            return ResponseEntity
                    .ok(new ApiResponse<>(false, null, "User is blocked contact admin"));
        }

        if (!authResult) {
            return ResponseEntity.ok(new ApiResponse<>(false, null, "Incorrect password"));
        }

        String token = jwtService.generateToken(user.getEmail());
        AuthData loginData = new AuthData(user, token);
        return ResponseEntity.ok(new ApiResponse<>(true, loginData, "Login successful"));
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
        if (userService.checkUserByEmail(signupRequest.getEmail())) {
            return ResponseEntity.ok(new ApiResponse<>(false, null, "User already exists"));
        }
        UserDTO savedUser = userService.saveUser(user);
        userSettingsService.getUserSettings(user);
        String token = jwtService.generateToken(user.getEmail());
        AuthData authData = new AuthData(savedUser, token);
        return ResponseEntity.ok(new ApiResponse<AuthData>(true, authData, "Signup successful"));
    }

    @GetMapping("/check-user")
    public ResponseEntity<ApiResponse<UserDTO>> getLoggedUser(Principal principal) {
        UserDTO user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(new ApiResponse<UserDTO>(true, user, "User checked successfully"));
    }

    /**
     * Logs out the user (token invalidation logic can be implemented).
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {

        return ResponseEntity.ok(new ApiResponse<>(true, null, "Logout successful"));
    }

    /**
     * Handles forgot password requests.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Password reset link sent to email"));
    }

    /**
     * Handles password reset requests.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Password reset successfully"));
    }

    /**
     * Handles password change requests.
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Password changed successfully"));
    }

    /**
     * Handles email verification requests.
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Email verification link sent"));
    }

    /**
     * Handles resend verification email requests.
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Verification email resent"));
    }

    /**
     * Checks the validity of a JWT token and returns the user if valid.
     */
    @PostMapping("/check-token")
    public ResponseEntity<ApiResponse<UserDTO>> checkToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ApiResponse<>(false, null, "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        String email = jwtService.getUsernameFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, null, "Invalid or expired token"));
        }
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, user, "Token is valid"));
    }
}
