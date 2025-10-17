package com.pawradise.api.controller;

import com.pawradise.api.dto.ReviewDto;
import com.pawradise.api.models.Product;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Autowired ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get a list of all products.
     * HTTP GET at /api/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> findAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> species,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> brand,
            Pageable pageable
    ) {
        Page<Product> products = productService.findProducts(search, species, category, brand, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }


    /**
     * Get a single product by its ID.
     * HTTP GET at /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Product found successfully"));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<Product>> addProductReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewDto reviewDto,
            Principal principal
    ) {
        String authorName = principal.getName(); // Or from a User object
        String userId = principal.getName(); // Or a unique ID
        Product updatedProduct = productService.addReview(id, userId, authorName, reviewDto);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Review added successfully"));
    }
}
