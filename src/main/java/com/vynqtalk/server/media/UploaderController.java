package com.vynqtalk.server.media;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1/upload")
public class UploaderController {

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private UserService userService;

    @PostMapping("/avatar/{id}")
    public ResponseEntity<ApiResponse<String>> uploadImage(@PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.ok(new ApiResponse<>("User don't exist", 403));
        }

        String response = imageUploadService.uploadImage(file);
        user.setAvatar(response);
        userService.updateUser(id,user);
        return ResponseEntity.ok(new ApiResponse<>(response, "Image uploaded successfully", 0));
    }

}
