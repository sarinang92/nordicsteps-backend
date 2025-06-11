// src/main/java/com/myproject/controller/OrderController.java
package com.myproject.controller;

import com.myproject.dto.OrderRequestDTO;
import com.myproject.dto.OrderResponseDTO;
import com.myproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint to place a new order.
     * User ID should ideally be retrieved from Spring Security context.
     *
     * @param userId Placeholder for authenticated user ID (replace with @AuthenticationPrincipal or similar).
     * @param orderRequestDTO The request body containing order details.
     * @return ResponseEntity with the created OrderResponseDTO.
     */
    @PostMapping("/checkout/{userId}") // userId will come from security context in real app
    public ResponseEntity<OrderResponseDTO> placeOrder(@PathVariable Long userId,
                                                     @RequestBody OrderRequestDTO orderRequestDTO) {
        // In a real application, userId should be obtained from the authenticated user's context (e.g., Spring Security)
        // For example: Long userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        // Or if you have a custom UserDetails implementation:
        // User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Long userId = currentUser.getId();

        OrderResponseDTO newOrder = orderService.placeOrder(userId, orderRequestDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all orders for a specific user.
     * User ID should ideally be retrieved from Spring Security context.
     *
     * @param userId Placeholder for authenticated user ID.
     * @return ResponseEntity with a list of OrderResponseDTOs.
     */
    @GetMapping("/user/{userId}") // userId will come from security context in real app
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint to get a specific order by ID.
     * Consider adding authorization to ensure only the order owner or admin can view it.
     *
     * @param orderId The ID of the order.
     * @return ResponseEntity with the OrderResponseDTO.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint to update the status of an order.
     * This endpoint should typically be restricted to ADMIN roles.
     *
     * @param orderId The ID of the order to update.
     * @param newStatus The new status string (e.g., "SHIPPED", "DELIVERED").
     * @return ResponseEntity with the updated OrderResponseDTO.
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                           @RequestParam String newStatus) {
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
}