package com.vynqtalk.server.controller.media;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.service.media.ImageService;

import org.springframework.http.HttpStatus;

import java.io.IOException;

@RestController
@RequestMapping("/public")
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

    @GetMapping("/message/{fileName}")
    public ResponseEntity<byte[]> getMessageFile(@PathVariable String fileName) {
        try {
            var fileWithType = imageService.getFileWithType(fileName);
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileWithType.getContentType()))
                .body(fileWithType.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}