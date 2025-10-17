package com.pawradise.api.service;


import com.pawradise.api.dto.ProductDto;
import com.pawradise.api.dto.ReviewDto;
import com.pawradise.api.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    /**
     * Finds and paginates products with optional filters for search, species, category, and brand.
     */
    Page<Product> findProducts(String search, List<String> species, List<String> category, List<String> brand, Pageable pageable);
    Product getProductById(String id);
    Product createProduct(ProductDto productdto, MultipartFile imageFile);
    Product updateProduct(String id, ProductDto productDto, MultipartFile imageFile);
    void deleteProduct(String id);
    Product addReview(String productId, String userId, String authorName, ReviewDto reviewDto);
}
