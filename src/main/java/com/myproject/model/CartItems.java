package com.myproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items", schema = "nordicsteps") // Updated schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(name = "fk_cart_item_cart"), nullable = false) // Added nullable=false
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_cart_item_product"), nullable = false) // Added nullable=false
    private Products product;

    @Column(length = 10, nullable = false)
    private String size;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;
}