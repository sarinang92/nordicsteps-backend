package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Add AllArgsConstructor for convenience

import java.math.BigDecimal;  // Import for BigDecimal
import java.time.LocalDateTime; // Import for LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor // Add this for full constructor convenience
public class ProductDetailDTO {
    private Long productId;           // Matches Products.productId
    private String name;              // Matches Products.name
    private String description;       // Matches Products.description
    private BigDecimal price;         // Matches Products.price (original price)
    private BigDecimal currentPrice;  // Matches Products.currentPrice
    private Integer totalStockQuantity; // Matches Products.totalStockQuantity
    private String color;             // Matches Products.color
    private String brand;             // Matches Products.brand
    private String areaOfUse;         // Matches Products.areaOfUse
    private String userTarget;        // Matches Products.userTarget
    private String imageUrl;          // Matches Products.imageUrl
    private Long categoryId;          // For mapping the category ID from Products.category
    private Boolean isNewArrival;     // Matches Products.isNewArrival
    private Boolean isBestseller;     // Matches Products.isBestseller
    private Boolean isOnSale;         // Matches Products.isOnSale
    private LocalDateTime createdAt;  // Matches Products.createdAt
}