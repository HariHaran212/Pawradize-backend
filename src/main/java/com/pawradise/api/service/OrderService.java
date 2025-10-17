package com.pawradise.api.service;

import com.pawradise.api.dto.OrderRequestDto;
import com.pawradise.api.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing product orders.
 */
public interface OrderService {
    // User
    /**
     * Creates a new order for a given user.
     * This involves validating stock, calculating the total, and decrementing product quantities.
     *
     * @param userId The ID of the user placing the order.
     * @param orderRequestDto The DTO containing the list of items and shipping address.
     * @return The newly created Order object.
     */
    Order createOrder(String username, OrderRequestDto orderRequestDto);

    /**
     * Retrieves all orders for a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of orders belonging to the user.
     */
    List<Order> getOrdersForUser(String username);

    /**
     * Retrieves a single order by its ID, ensuring it belongs to the specified user.
     *
     * @param orderId The ID of the order to retrieve.
     * @param userId The ID of the user who must own the order.
     * @return The found Order object.
     * @throws com.pawradise.api.exception.OrderNotFoundException if the order doesn't exist or doesn't belong to the user.
     */
    Order getOrderByIdAndUserId(String orderId, String userId);

    // Admin
    /**
     * Retrieves all orders in the system. For admin use only.
     * @return A list of all orders.
     */
    Page<Order> getAllOrders(String search, Order.OrderStatus status, Pageable pageable);

    /**
     * Retrieves a single order by its ID without checking the user. For admin use only.
     * @param orderId The ID of the order to retrieve.
     * @return The found Order object.
     */
    Order getOrderByIdForAdmin(String orderId);

    /**
     * Updates the status of an existing order. For admin use only.
     * @param orderId The ID of the order to update.
     * @param newStatus The new status for the order.
     * @return The updated Order object.
     */
    Order updateOrderStatus(String orderId, Order.OrderStatus newStatus);
}