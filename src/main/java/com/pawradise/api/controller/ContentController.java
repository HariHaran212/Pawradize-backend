package com.pawradise.api.controller;

import com.pawradise.api.models.Content;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(@Autowired ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * Get all content. Publicly accessible.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Content>>> getAllPublicContent() {
        List<Content> contentList = contentService.getAllPublishedContent();
        return ResponseEntity.ok(ApiResponse.success(contentList, "Content retrieved successfully"));
    }

    /**
     * Get a single piece of content by its slug. Publicly accessible.
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Content>> getPublicContentBySlug(@PathVariable String slug) {
        Content content = contentService.getPublishedContentBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(content, "Content found successfully"));
    }
}
