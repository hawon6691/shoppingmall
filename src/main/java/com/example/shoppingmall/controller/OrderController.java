package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.OrderRequest;
import com.example.shoppingmall.dto.OrderResponse;
import com.example.shoppingmall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order from cart items")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestAttribute("userId") Long userId,
            @RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @Operation(summary = "Get user's orders", description = "Retrieve all orders for the current user")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@RequestAttribute("userId") Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderById(userId, orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Update the status of an order (Admin only)")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        OrderResponse order = orderService.updateOrderStatus(orderId, newStatus.toUpperCase());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel a pending or confirmed order")
    public ResponseEntity<OrderResponse> cancelOrder(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long orderId) {

        OrderResponse order = orderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(order);
    }
}
