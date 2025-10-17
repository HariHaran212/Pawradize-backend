package com.pawradise.api.service.impl;

import com.pawradise.api.dto.ProductDto;
import com.pawradise.api.dto.ReviewDto;
import com.pawradise.api.models.Product;
import com.pawradise.api.repository.ProductRepository;
import com.pawradise.api.exception.ProductNotFoundException;

import com.pawradise.api.service.CloudinaryService;
import com.pawradise.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final MongoTemplate mongoTemplate;

    public ProductServiceImpl(@Autowired ProductRepository productRepository,  @Autowired CloudinaryService cloudinaryService, @Autowired MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Retrieves all products from the database.
     * @return a list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            System.out.println(product);
        }
        return products;
    }

    @Override
    public Page<Product> findProducts(String search, List<String> species, List<String> category, List<String> brand, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteriaList = new ArrayList<>();

//        criteriaList.add(Criteria.where("stockQuantity").gt(0));

        // Add search term filter (searches name and description)
        if (search != null && !search.trim().isEmpty()) {
            Pattern searchPattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("name").regex(searchPattern),
                    Criteria.where("description").regex(searchPattern)
            ));
        }

        // Add species filter (checks if the species is in the product's list)
        if (species != null && !species.isEmpty()) {
            criteriaList.add(Criteria.where("featuredSpecies").in(species));
        }

        // Add category filter
        if (category != null && !category.isEmpty()) {
            criteriaList.add(Criteria.where("category").in(category));
        }

        // Add brand filter
        if (brand != null && !brand.isEmpty()) {
            criteriaList.add(Criteria.where("brand").in(brand));
        }

        // Combine all filters with an AND operator
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // Fetch the paginated list of products
        List<Product> products = mongoTemplate.find(query, Product.class);

        // Use PageableExecutionUtils to create a Page object with the total count
        return PageableExecutionUtils.getPage(
                products,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class)
        );
    }

    /**
     * Finds a single product by its ID.
     * @param id The ID of the product to find.
     * @return The found product.
     * @throws ProductNotFoundException if no product with the given ID is found.
     */
    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    /**
     * Creates and saves a new product.
     * @param productDto The product object to save.
     * @return The saved product, including its generated ID.
     */
    @Override
    public Product createProduct(ProductDto productDto, MultipartFile imageFile) {

        // 1. Create the new Product object
        Product product = new Product();
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setBrand(productDto.getBrand());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setFeaturedSpecies(productDto.getFeaturedSpecies());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);
    }

    /**
     * Updates an existing product's details.
     * @param id The ID of the product to update.
     * @param productDto The new details for the product.
     * @return The updated product.
     * @throws ProductNotFoundException if the product to update is not found.
     */
    @Override
    public Product updateProduct(String id, ProductDto productDto, MultipartFile imageFile) {
        Product existingProduct = getProductById(id);

        // Get the old image URL before any changes are made
        String oldImageUrl = existingProduct.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            // A new file has been uploaded

            // 1. Upload the new file and get its URL
            String newImageUrl = cloudinaryService.uploadFile(imageFile);
            existingProduct.setImageUrl(newImageUrl);

            // 2. If an old image existed, delete it from Cloudinary
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                cloudinaryService.deleteFileByUrl(oldImageUrl);
            }
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setBrand(productDto.getBrand());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());
        existingProduct.setFeaturedSpecies(productDto.getFeaturedSpecies());

        return productRepository.save(existingProduct);
    }

    /**
     * Deletes a product from the database.
     * @param id The ID of the product to delete.
     * @throws ProductNotFoundException if the product to delete is not found.
     */
    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product addReview(String productId, String userId, String authorName, ReviewDto reviewDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Check if user has already reviewed
        boolean hasReviewed = product.getReviews().stream().anyMatch(r -> r.getUserId().equals(userId));
        if (hasReviewed) {
            throw new IllegalStateException("You have already reviewed this product.");
        }

        Product.Review review = new Product.Review();
        review.setUserId(userId);
        review.setAuthorName(authorName);
        review.setRating(reviewDto.getRating());
        review.setText(reviewDto.getText());
        review.setCreatedAt(LocalDateTime.now());

        product.getReviews().add(review);
        product.setNumReviews(product.getReviews().size());
        product.setAverageRating(
                product.getReviews().stream().mapToDouble(Product.Review::getRating).average().orElse(0.0)
        );

        return productRepository.save(product);
    }
}