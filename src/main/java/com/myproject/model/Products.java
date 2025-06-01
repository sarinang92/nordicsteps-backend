package com.myproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", schema = "nordicsteps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "current_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "total_stock_quantity", nullable = false)
    private Integer totalStockQuantity;

    @Column(length = 50)
    private String color;

    @Column(length = 100)
    private String brand;

    @Column(name = "area_of_use", length = 100)
    private String areaOfUse;

    @Column(name = "user_target", length = 50)
    private String userTarget;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private Categories category;

    @Column(name = "is_new_arrival")
    private boolean isNewArrival = false;

    @Column(name = "is_bestseller")
    private boolean isBestseller = false;

    @Column(name = "is_on_sale")
    private boolean isOnSale = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
