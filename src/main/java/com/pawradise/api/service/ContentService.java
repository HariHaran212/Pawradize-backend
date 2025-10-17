package com.pawradise.api.service;

import com.pawradise.api.models.Content;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {
    List<Content> getAllPublishedContent();
    Content getPublishedContentBySlug(String slug);
    List<Content> getAllContent();
    Content getContentBySlug(String slug);
    Content createContent(Content content, MultipartFile imageFile);
    Content updateContent(String id, Content content, MultipartFile imageFile);
    void deleteContent(String id);
}
