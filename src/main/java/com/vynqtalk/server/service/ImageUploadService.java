package com.vynqtalk.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageUploadService {

    private static final String UPLOAD_DIR = "uploads/"; // Relative to project root
    private static final String[] ALLOWED_TYPES = { "image/jpeg", "image/png", "image/gif" };
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public String uploadImage(MultipartFile file, String username) {
        try {
            // Validate file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("No file uploaded");
            }

            // Validate file size
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds 10MB limit");
            }

            // Validate file type
            String contentType = file.getContentType();
            System.out.println("Content type " + contentType);
            boolean isValidType = Arrays.stream(ALLOWED_TYPES).anyMatch(type -> type.equalsIgnoreCase(contentType));
            if (!isValidType) {
                throw new IllegalArgumentException("Invalid file type. Allowed: JPEG, PNG, GIF");
            }

            // Create upload directory
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                // Set permissions for Deepin Linux
                uploadPath.toFile().setWritable(true, false); // Owner and group writable
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            System.out.println("ORiginal file name " + originalFilename);
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            System.out.println("Final file name " + extension);
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uniqueFilename);

            // Save file
            Files.write(filePath, file.getBytes());

            // Return file URL
            return "/uploads/" + uniqueFilename;
        } catch (Error | IOException e) {
            return "There have been error";
        }
    }
}