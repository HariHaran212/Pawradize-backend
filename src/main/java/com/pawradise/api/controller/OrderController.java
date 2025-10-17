package com.pawradise.api.controller;

import com.pawradise.api.dto.OrderRequestDto;
import com.pawradise.api.models.Order;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/orders : Creates a new order for the authenticated user.
     *
     * @param orderRequestDto The request body containing order items and shipping address.
     * @param principal The currently authenticated user provided by Spring Security.
     * @return A response entity with the created order and a 201 status code.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> placeOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto,
            Principal principal
    ) {
        // Get the user's unique identifier (e.g., email or sub) from the security context
        String username = principal.getName();
        Order createdOrder = orderService.createOrder(username, orderRequestDto);
        return new ResponseEntity<>(
                ApiResponse.success(createdOrder, "Order placed successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * GET /api/orders : Retrieves the order history for the authenticated user.
     *
     * @param principal The currently authenticated user.
     * @return A response entity with a list of the user's orders.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(Principal principal) {
        String username = principal.getName();
        List<Order> orders = orderService.getOrdersForUser(username);
        return ResponseEntity.ok(ApiResponse.success(orders, "User orders retrieved successfully"));
    }

    /**
     * GET /api/orders/{orderId} : Retrieves a single order by its ID for the authenticated user.
     *
     * @param orderId The ID of the order to retrieve.
     * @param principal The currently authenticated user.
     * @return A response entity with the details of the specific order.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getUserOrderById(
            @PathVariable String orderId,
            Principal principal
    ) {
        String userId = principal.getName();
        Order order = orderService.getOrderByIdAndUserId(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(order, "Order details retrieved successfully"));
    }


}