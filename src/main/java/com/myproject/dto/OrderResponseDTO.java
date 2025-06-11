package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Long userId; // The ID of the user who placed the order
    private LocalDateTime orderDate;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private String deliveryMethod;
    private String discountCode; // Display the applied discount code (if any)
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemResponseDTO> orderItems; // List of items in the order
}