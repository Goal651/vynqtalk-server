package com.vynqtalk.server.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.service.UploadService;
import com.vynqtalk.server.service.UserService;
import com.vynqtalk.server.model.users.User;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/upload")
public class UploaderController {

        private final UploadService imageUploadService;
        private final UserService userService;

        public UploaderController(UploadService imageUploadService, UserService userService) {
                this.imageUploadService = imageUploadService;
                this.userService = userService;
        }

        @PostMapping("/user")
        public ResponseEntity<ApiResponse<String>> uploadUserProfileImage(Principal principal,
                        @RequestParam("file") MultipartFile file,
                        HttpServletRequest request) {
                User user = userService.getUserByEmail(principal.getName());
                String fileName = imageUploadService.uploadImage(file);
                String baseUrl = request.getScheme() + "://" + request.getServerName() +
                                ((request.getServerPort() == 80 || request.getServerPort() == 443) ? ""
                                                : ":" + request.getServerPort());
                String response = baseUrl + "/api/v2/public/profile/" + fileName;
                user.setAvatar(response);
                userService.updateUser(user);
                return ResponseEntity.ok(new ApiResponse<>(true,response, "Image uploaded successfully"));
        }

        @PostMapping("/message")
        public ResponseEntity<ApiResponse<String>> uploadMessageFile(@RequestParam("file") MultipartFile file,
                        HttpServletRequest request) {
                String fileName = imageUploadService.uploadMessage(file);
                String baseUrl = request.getScheme() + "://" + request.getServerName() +
                                ((request.getServerPort() == 80 || request.getServerPort() == 443) ? ""
                                                : ":" + request.getServerPort());
                String response = baseUrl + "/api/v2/public/message/" + fileName;
                return ResponseEntity.ok(new ApiResponse<>(true,response, "Image uploaded successfully"));
        }

        @PostMapping("/group/{id}")
        public ResponseEntity<ApiResponse<String>> uploadGroupProfileImage(@PathVariable Long id,
                        @RequestParam("file") MultipartFile file) {
                throw new UnsupportedOperationException(
                                "Group profile image upload not implemented. Use GroupService and Group entity.");
        }
}