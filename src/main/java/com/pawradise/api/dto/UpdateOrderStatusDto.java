package com.pawradise.api.dto;

import com.pawradise.api.models.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    @NotNull(message = "New status cannot be null.")
    private Order.OrderStatus status;
}