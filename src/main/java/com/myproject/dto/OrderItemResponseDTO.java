// src/main/java/com/myproject/dto/OrderItemResponseDTO.java
package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private String size;
    private Integer quantity;
    private BigDecimal priceEach; // Price at the time of order for this item
}