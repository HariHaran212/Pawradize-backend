package com.pawradise.api.repository;

import com.pawradise.api.models.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends MongoRepository<Content, String> {
    List<Content> findAllByStatus(String status);
    Optional<Content> findBySlug(String slug);
    Optional<Content> findBySlugAndStatus(String slug, String status);
}
