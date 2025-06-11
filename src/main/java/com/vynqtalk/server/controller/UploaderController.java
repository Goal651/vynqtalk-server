package com.vynqtalk.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.service.ImageUploadService;

@RestController
@RequestMapping("/api/v1/upload")
public class UploaderController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {

        String response = imageUploadService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>(response, "Image uploaded successfully", 0));
    }

}
