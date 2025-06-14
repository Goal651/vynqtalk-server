package com.vynqtalk.server.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {



    public byte[] getImage(String fileName) throws IOException {
        Path path = Paths.get("uploads/" + fileName);
        return Files.readAllBytes(path);
    }
}