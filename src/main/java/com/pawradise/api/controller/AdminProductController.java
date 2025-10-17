package com.pawradise.api.controller;

import com.pawradise.api.dto.ProductDto;
import com.pawradise.api.dto.ReviewDto;
import com.pawradise.api.models.Product;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
//@CrossOrigin(origins = "http://localhost:5173") // Or your global CORS config
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(@Autowired ProductService productService) {
        this.productService = productService;
    }

    /**
     * Create a new product.
     * HTTP POST at /api/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestPart("productData") ProductDto productDto,
                                                              @RequestPart(value = "imageFile") MultipartFile imageFile) {
        Product newProduct = productService.createProduct(productDto,  imageFile);
        return new ResponseEntity<>(ApiResponse.success(newProduct, "Product created successfully"), HttpStatus.CREATED);
    }

    /**
     * Get a list of all products.
     * HTTP GET at /api/admin/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "All products retrieved successfully"));
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

    /**
     * Update an existing product.
     * HTTP PUT at /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable String id,
                                                              @Valid @RequestPart("productData") ProductDto productDto,
                                                              @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        Product updatedProduct = productService.updateProduct(id, productDto, imageFile);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"));
    }

    /**
     * Delete a product by its ID.
     * HTTP DELETE at /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }
}