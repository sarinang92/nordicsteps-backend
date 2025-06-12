// src/main/java/com/myproject/model/Products.java
package com.myproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Added for completeness
import lombok.AllArgsConstructor; // Added for completeness

import java.math.BigDecimal;
import java.time.LocalDateTime; // Added for created_at

@Entity
@Setter
@Getter
@NoArgsConstructor // Added for completeness
@AllArgsConstructor // Added for completeness
@Table(name = "products", schema = "nordicsteps")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, length = 100) // Corrected length from 255 to 100 as per SQL
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2) // Added original price field
    private BigDecimal price;

    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "total_stock_quantity", nullable = false) // Mapped to SQL's total_stock_quantity
    private Integer totalStockQuantity; // Changed from stockQuantity

    @Column(length = 50)
    private String color; // Added field

    @Column(length = 100)
    private String brand; // Added field

    @Column(name = "area_of_use", length = 100)
    private String areaOfUse; // Added field

    @Column(name = "user_target", length = 50)
    private String userTarget; // Added field

    @Column(name = "image_url", length = 255) // Corrected length from 500 to 255 as per SQL
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private Categories category; // Assuming you have a Categories entity

    @Column(name = "is_new_arrival", nullable = false)
    private Boolean isNewArrival = false; // Added field, default false

    @Column(name = "is_bestseller", nullable = false)
    private Boolean isBestseller = false; // Added field, default false

    @Column(name = "is_on_sale", nullable = false)
    private Boolean isOnSale = false; // Added field, default false

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Added field

    @Column(length = 50) // Added field for size
    private String size;

    @Column(length = 100) // Added field for campaign
    private String campaign;

    @Column(length = 100) // Added field for area
    private String area; 

    @PrePersist // Set createdAt when entity is first persisted
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}