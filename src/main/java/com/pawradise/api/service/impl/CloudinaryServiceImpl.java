package com.pawradise.api.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pawradise.api.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }

    // --- ADD THIS METHOD ---
    @Override
    public void deleteFileByUrl(String imageUrl) {
        try {
            // 1. Extract the Public ID from the full URL
            String publicId = extractPublicIdFromUrl(imageUrl);

            // 2. Call the destroy method to delete the image
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
    }

    // --- ADD THIS HELPER METHOD ---
    private String extractPublicIdFromUrl(String imageUrl) {
        // Example URL: http://res.cloudinary.com/your_cloud_name/image/upload/v1612345678/unique_file_name.jpg
        // We want to extract "unique_file_name"
        int lastSlash = imageUrl.lastIndexOf('/');
        int lastDot = imageUrl.lastIndexOf('.');
        if (lastSlash != -1 && lastDot != -1 && lastSlash < lastDot) {
            return imageUrl.substring(lastSlash + 1, lastDot);
        }
        throw new IllegalArgumentException("Invalid Cloudinary URL format");
    }
}