package com.pawradise.api.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime lastUpdated;

    @Data
    public static class CartItem {
        private String productId;
        private int quantity;
    }
}