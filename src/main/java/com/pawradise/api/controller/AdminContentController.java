package com.pawradise.api.controller;

import com.pawradise.api.models.Content;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/content")
public class AdminContentController {

    private final ContentService contentService;

    public AdminContentController(@Autowired ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * Get all content.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Content>>> getAllContent() {
        List<Content> contentList = contentService.getAllContent();
        return ResponseEntity.ok(ApiResponse.success(contentList, "Content retrieved successfully"));
    }

    /**
     * Get a single piece of content by its slug.
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Content>> getContentBySlug(@PathVariable String slug) {
        Content content = contentService.getContentBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(content, "Content found successfully"));
    }

    /**
     * Create new content. Should be secured for admin users.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Content>> createContent(@Valid @RequestPart("contentData") Content content,
                                                              @RequestPart(value = "heroImageFile", required = false) MultipartFile imageFile) {
        Content newContent = contentService.createContent(content, imageFile);
        return new ResponseEntity<>(ApiResponse.success(newContent, "Content created successfully"), HttpStatus.CREATED);
    }

    /**
     * Update existing content by ID. Should be secured for admin users.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Content>> updateContent(@PathVariable String id,
                                                              @Valid @RequestPart("contentData") Content content,
                                                              @RequestPart(value = "heroImageFile", required = false) MultipartFile imageFile) {
        Content updatedContent = contentService.updateContent(id, content, imageFile);
        return ResponseEntity.ok(ApiResponse.success(updatedContent, "Content updated successfully"));
    }

    /**
     * Delete content by ID. Should be secured for admin users.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(@PathVariable String id) {
        contentService.deleteContent(id);
        return ResponseEntity.ok(ApiResponse.success("Content deleted successfully"));
    }
}