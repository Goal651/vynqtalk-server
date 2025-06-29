package com.vynqtalk.server.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.vynqtalk.server.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/public")
public class MediaController {

    private final ImageService imageService;

    public MediaController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/profile/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] image = imageService.getImage(fileName);
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}