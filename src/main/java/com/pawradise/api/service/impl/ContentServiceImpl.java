package com.pawradise.api.service.impl;

import com.pawradise.api.models.Content;
import com.pawradise.api.repository.ContentRepository;
import com.pawradise.api.exception.ContentNotFoundException;
import com.pawradise.api.service.CloudinaryService;
import com.pawradise.api.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final CloudinaryService cloudinaryService;

    public ContentServiceImpl(@Autowired ContentRepository contentRepository, @Autowired CloudinaryService cloudinaryService) {
        this.contentRepository = contentRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<Content> getAllPublishedContent() {
        return contentRepository.findAllByStatus("Published");
    }

    @Override
    public Content getPublishedContentBySlug(String slug) {
        return contentRepository.findBySlugAndStatus(slug, "Published")
                .orElseThrow(() -> new ContentNotFoundException("Content not found with slug: " + slug));
    }

    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    public Content getContentBySlug(String slug) {
        return contentRepository.findBySlug(slug)
                .orElseThrow(() -> new ContentNotFoundException("Content not found with slug: " + slug));
    }

    public Content createContent(Content content, MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            content.setHeroImageUrl(imageUrl);
        }

        return contentRepository.save(content);
    }

    @Override
    public Content updateContent(String id, Content content, MultipartFile imageFile) {
        Content existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Content not found with id: " + id));

        existingContent.setTitle(content.getTitle());
        existingContent.setSubtitle(content.getSubtitle());
        existingContent.setSnippet(content.getSnippet());
        existingContent.setContent(content.getContent());
        existingContent.setCta(content.getCta());
        existingContent.setStatus(content.getStatus());
        existingContent.setAuthor(content.getAuthor());


        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            existingContent.setHeroImageUrl(imageUrl);
        }

        return contentRepository.save(existingContent);
    }

    public void deleteContent(String id) {
        if (!contentRepository.existsById(id)) {
            throw new ContentNotFoundException("Content not found with id: " + id);
        }
        contentRepository.deleteById(id);
    }
}