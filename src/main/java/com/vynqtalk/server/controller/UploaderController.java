package com.vynqtalk.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.service.ImageUploadService;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.error.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/upload")
public class UploaderController {

    private final ImageUploadService imageUploadService;
    private final UserService userService;

    public UploaderController(ImageUploadService imageUploadService, UserService userService) {
        this.imageUploadService = imageUploadService;
        this.userService = userService;
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<ApiResponse<String>> uploadUserProfileImage(@PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        String fileName = imageUploadService.uploadImage(file);
        String baseUrl = request.getScheme() + "://" + request.getServerName() +
            ((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
        String response = baseUrl + "/api/v1/public/profile/" + fileName;
        user.setAvatar(response);
        userService.updateUser(id, user);
        return ResponseEntity.ok(new ApiResponse<>(response, "Image uploaded successfully", 200));
    }

    @PostMapping("/group/{id}")
    public ResponseEntity<ApiResponse<String>> uploadGroupProfileImage(@PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        throw new UnsupportedOperationException("Group profile image upload not implemented. Use GroupService and Group entity.");
    }
}