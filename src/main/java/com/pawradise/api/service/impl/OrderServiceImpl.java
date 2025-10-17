package com.pawradise.api.service.impl;

import com.pawradise.api.dto.OrderRequestDto;
import com.pawradise.api.exception.IllegalStateException;
import com.pawradise.api.exception.OrderNotFoundException;
import com.pawradise.api.exception.ProductNotFoundException;
import com.pawradise.api.models.Order;
import com.pawradise.api.models.Product;
import com.pawradise.api.models.User;
import com.pawradise.api.repository.OrderRepository;
import com.pawradise.api.repository.ProductRepository;
import com.pawradise.api.repository.UserRepository;
import com.pawradise.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    // USER METHODS

    /**
     * Creates a new order. This is a transactional operation that includes:
     * 1. Validating product existence and stock levels.
     * 2. Calculating the total order amount based on current product prices.
     * 3. Saving the new order to the database.
     * 4. Decrementing the stock quantity for each product ordered.
     */
    @Override
    @Transactional
    public Order createOrder(String username, OrderRequestDto orderRequestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Order order = new Order();

        Order.Customer customer = new  Order.Customer();

        customer.setCustomerId(user.getId());
        customer.setCustomerName(orderRequestDto.getFullName());
        customer.setEmail(orderRequestDto.getEmail());
        customer.setPhoneNumber(orderRequestDto.getPhone());
        order.setCustomer(customer);

        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setStatus(Order.OrderStatus.PENDING);

        List<Order.OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;

        // Validate items and calculate total
        for (OrderRequestDto.OrderItemDto itemDto : orderRequestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + itemDto.getProductId()));

            if (product.getStockQuantity() < itemDto.getQuantity()) {
                throw new IllegalStateException("Not enough stock for product: " + product.getName());
            }

            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setImageUrl(product.getImageUrl());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItems.add(orderItem);

            subtotal += product.getPrice() * itemDto.getQuantity();
        }

        double shippingFee = subtotal > 2000 ? 0 : 50; // Your business logic for shipping
        double taxes = subtotal * 0.18; // Your business logic for tax
        double totalAmount = subtotal + shippingFee + taxes;

        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setTaxes(taxes);
        order.setTotalAmount(totalAmount);

        // Save the order first
        Order savedOrder = orderRepository.save(order);

        // Then, update product stock
        for (Order.OrderItem item : savedOrder.getOrderItems()) {
            // We use .get() here because we've already confirmed the product exists.
            Product product = productRepository.findById(item.getProductId()).get();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        return savedOrder;
    }

    /**
     * Retrieves a list of all orders placed by a specific user.
     */
    @Override
    public List<Order> getOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return orderRepository.findByCustomerCustomerId(user.getId());
    }

    /**
     * Retrieves a single order by its ID, but only if it belongs to the specified user.
     * This prevents one user from accessing another user's order details.
     */
    @Override
    public Order getOrderByIdAndUserId(String orderId, String username) {
        // Find the order by its ID first
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        String userId = user.getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        // Security check: ensure the found order belongs to the requesting user
        if (!order.getCustomer().getCustomerId().equals(userId)) {
            // Throw the same exception to avoid revealing that the order exists
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        return order;
    }

    // ADMIN METHODS

    @Override
    public Page<Order> getAllOrders(String search, Order.OrderStatus status, Pageable pageable) {
        final Query query = new Query().with(pageable);
        final List<Criteria> criteria = new ArrayList<>();

        // Add status filter if provided
        if (status != null) {
            criteria.add(Criteria.where("status").is(status));
        }

        // Add search term filter if provided
        if (search != null && !search.trim().isEmpty()) {
            Pattern searchPattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

            criteria.add(new Criteria().orOperator(
                    Criteria.where("id").regex(searchPattern),
                    Criteria.where("customer.customerName").regex(searchPattern)
            ));
        }

        // Combine all criteria with an AND operator
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        Query countQuery = Query.of(query);

        // Fetch the list of orders for the current page
        List<Order> orders = mongoTemplate.find(query, Order.class);

        // Return a Page object, which includes the list and pagination metadata
        return PageableExecutionUtils.getPage(
                orders,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Order.class)
        );
    }

    @Override
    public Order getOrderByIdForAdmin(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public Order updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }
}