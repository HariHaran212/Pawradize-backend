package com.pawradise.api.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "content")
public class Content {

    @Id
    private String id;

    @NotBlank(message = "Slug cannot be empty")
    @Size(max = 200)
    @Indexed(unique = true)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be in a URL-friendly format (e.g., 'how-to-train-your-puppy')")
    private String slug;

    @NotBlank
    @Size(max = 100)
    private String subtitle;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    private String heroImageUrl; // URL to the uploaded hero image

    @NotBlank
    @Size(max = 500)
    private String snippet; // A short teaser text

    @NotBlank(message = "Body content cannot be empty")
    private String content; // The main article content, stored as an HTML string

    @NotNull
    @Valid // This ensures the nested CTA object is also validated
    private CallToAction cta;

    @NotBlank
    private String status; // e.g., "Published", "Draft"

    @NotBlank(message = "Author cannot be empty")
    private String author;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Nested class to represent the Call to Action block.
     */
    @Data
    @NoArgsConstructor
    public static class CallToAction {
        @NotBlank
        private String title;
        @NotBlank
        private String text;
        @NotBlank
        private String buttonText;
        @NotBlank
        private String buttonLink;
    }
}