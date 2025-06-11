package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    // User ID is typically taken from security context, not directly in DTO
    // private Long userId; // Removed, as it's typically derived from authentication

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Delivery method is required")
    private String deliveryMethod;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Optional: If you allow discount codes to be applied at checkout
    private String discountCode; // This would be the actual discount code string
}