// src/main/java/com/pawradise/api/dto/OrderRequestDto.java
package com.pawradise.api.dto;

import com.pawradise.api.models.Order.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull
    private String fullName;

    @NotEmpty(message = "Order must contain at least one item.")
    @Valid // This enables validation on the nested OrderItemDto objects
    private List<OrderItemDto> items;

    @NotNull(message = "Shipping address cannot be null.")
    @Valid
    private Address shippingAddress;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    @Data
    public static class OrderItemDto {
        @NotBlank(message = "Product ID cannot be empty.")
        private String productId;

        @Min(value = 1, message = "Quantity must be at least 1.")
        private int quantity;
    }
}