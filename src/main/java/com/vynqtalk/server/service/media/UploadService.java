package com.vynqtalk.server.service.media;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UploadService {

    private static final String UPLOAD_DIR = "uploads/"; 
    private static final String[] ALLOWED_TYPES = { "image/jpg","image/jpeg", "image/png", "image/gif" };
    private static final long MAX_FILE_SIZE = 30 * 1024 * 1024; // 10MB

    public String uploadImage(MultipartFile file) {
        try {
            System.out.println("File size ...................................."+file.getSize());

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
                uploadPath.toFile().setWritable(true, false); 
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uniqueFilename);

            // Save file
            Files.write(filePath, file.getBytes());

            // Return file URL
            return  uniqueFilename;
        } catch (Error | IOException e) {
            return null;
        }
    }

    public String uploadMessage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("No file uploaded");
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds 10MB limit");
            }

            // Accept any file type, no type check

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                uploadPath.toFile().setWritable(true, false); 
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.write(filePath, file.getBytes());

            // Return only the unique filename
            return uniqueFilename;
        } catch (Error | IOException e) {
            return null;
        }
    }

}