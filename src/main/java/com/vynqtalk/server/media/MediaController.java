package com.vynqtalk.server.media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vynqtalk.server.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/public")
public class MediaController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] image = imageService.getImage(fileName);
        return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(image);
    }
}