package com.pawradise.api.controller;

import com.pawradise.api.models.Order;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * GET /api/admin/orders : Retrieves all orders in the system.
     * (Requires ADMIN role)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Order>>> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Order.OrderStatus status,
            Pageable pageable) {
        Page<Order> allOrders = orderService.getAllOrders(search, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(allOrders, "All orders retrieved successfully"));
    }

    /**
     * GET /api/admin/orders/{orderId} : Retrieves a single order by ID.
     * (Requires ADMIN role)
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderByIdForAdmin(orderId);
        return ResponseEntity.ok(ApiResponse.success(order, "Order details retrieved successfully"));
    }

    /**
     * PUT /api/admin/orders/{orderId}/status : Updates the status of an order.
     * (Requires ADMIN role)
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam("newStatus") Order.OrderStatus newStatus
    ) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(ApiResponse.success(updatedOrder, "Order status updated successfully"));
    }
}