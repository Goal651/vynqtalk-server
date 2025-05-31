package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.response.ApiResponse;
import com.vynqtalk.server.model.response.AuthResult;
import com.vynqtalk.server.model.response.LoginData;
import com.vynqtalk.server.model.response.SignupData;
import com.vynqtalk.server.model.response.SignupResponse;
import com.vynqtalk.server.service.JwtService;
import com.vynqtalk.server.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginData>> login(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email and password are required", HttpStatus.BAD_REQUEST.value()));
        }
        AuthResult authResult = userService.authenticate(user);
        if (!authResult.isAuth()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value()));
        }
        System.out.println("Loaded users " + authResult.toString());
        User authenticatedUser = authResult.getUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value()));
        }

        String token = jwtService.generateToken(authenticatedUser.getEmail());

        return ResponseEntity.ok(new ApiResponse<LoginData>(new LoginData(authenticatedUser, token), "Login successful",
                HttpStatus.OK.value(),
                authenticatedUser));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse<SignupData>> signup(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            return ResponseEntity.badRequest().body(
                    new SignupResponse<>(null, "Email, password, and name are required"));
        }
        user.setIsAdmin(false); // Default to non-admin user
        // Check if user already exists
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SignupResponse<>(null, "User already exists"));
        }
        userService.saveUser(user);
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SignupResponse<SignupData>(new SignupData(token, user), "Signup successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // Invalidate the JWT token logic can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Logout successful", HttpStatus.OK.value()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh() {
        // Refresh the JWT token logic can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Token refreshed successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to handle forgot password (e.g., send reset link) can be implemented
        // here
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset link sent to email", HttpStatus.OK.value()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(null, "Email and new password are required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to reset the password can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Password reset successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(null, "Email and new password are required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to change the password can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Password changed successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to verify the email can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Email verification link sent", HttpStatus.OK.value()));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(@RequestBody User user) {
        if (user.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Email is required", HttpStatus.BAD_REQUEST.value()));
        }
        // Logic to resend the verification email can be implemented here
        // For now, we will just return a success message
        return ResponseEntity.ok(new ApiResponse<>(null, "Verification email resent", HttpStatus.OK.value()));
    }

}
