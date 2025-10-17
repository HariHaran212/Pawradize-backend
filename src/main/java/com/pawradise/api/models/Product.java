package com.pawradise.api.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "SKU cannot be empty")
    @Size(max = 50, message = "SKU must be less than 50 characters")
    @Indexed(unique = true) // This tells MongoDB to enforce uniqueness on this field
    private String sku;

    @NotBlank(message = "Product name cannot be empty")
    @Size(max = 100, message = "Product name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @Positive(message = "Price must be a positive number")
    private double price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @NotBlank(message = "Image URL cannot be empty")
    @URL(message = "Must be a valid URL")
    // For stricter validation, you can add the hibernate-validator dependency
    // and use @URL(message = "Must be a valid URL")
    private String imageUrl;

    private List<String> featuredSpecies;

    private double averageRating = 0.0;
    private int numReviews = 0;
    private List<Review> reviews = new ArrayList<>();

    // Nested class for reviews
    @Data
    @NoArgsConstructor
    public static class Review {
        private String userId;
        private String authorName;
        private int rating;
        private String text;
        private LocalDateTime createdAt;
    }
}