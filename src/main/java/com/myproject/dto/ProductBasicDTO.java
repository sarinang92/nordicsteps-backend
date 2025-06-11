package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Import for BigDecimal

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasicDTO {
    private Long productId;      // Matches Products.productId
    private String name;         // Matches Products.name
    private BigDecimal currentPrice; // Matches Products.currentPrice (and uses BigDecimal)
    private String imageUrl;     // Matches Products.imageUrl
    private Boolean isOnSale;    // Matches Products.isOnSale
}