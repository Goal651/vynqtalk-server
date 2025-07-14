package com.vynqtalk.server.service.media;

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

    public static class FileWithType {
        private final byte[] bytes;
        private final String contentType;
        public FileWithType(byte[] bytes, String contentType) {
            this.bytes = bytes;
            this.contentType = contentType;
        }
        public byte[] getBytes() { return bytes; }
        public String getContentType() { return contentType; }
    }

    public FileWithType getFileWithType(String fileName) throws IOException {
        Path path = Paths.get("uploads/" + fileName);
        byte[] bytes = Files.readAllBytes(path);
        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = "application/octet-stream";
        return new FileWithType(bytes, contentType);
    }
}