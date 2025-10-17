package com.pawradise.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private Customer customer;
    private List<OrderItem> orderItems;
    private Address shippingAddress;
    private double subtotal;
    private double shippingFee;
    private double taxes;
    private double totalAmount;
    private OrderStatus status;
    @CreatedDate
    private LocalDateTime orderDate;

    @Data
    @NoArgsConstructor
    public static class Customer{
        String customerId;
        String customerName;
        String email;
        String phoneNumber;
    }

    // A nested class for the items within the order
    @Data
    @NoArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private String imageUrl;
        private int quantity;
        private double price;
    }

    // A nested class for the shipping address
    @Data
    @NoArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}