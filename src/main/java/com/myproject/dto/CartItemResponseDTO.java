package com.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable; // Keep this import if you intend to use @Nullable for optional fields

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    private Long cartItemId; // Corresponds to the cart_item_id in the database entity
    private Long productId;
    private int quantity;
    private String size;

    // Fields for display, populated from the associated Product
    @Nullable
    private String productName;
    @Nullable
    private String productImageUrl;
    @Nullable
    private BigDecimal productCurrentPrice;
    @Nullable
    private BigDecimal totalPrice; // Total price for this specific cart item (quantity * productCurrentPrice)
}