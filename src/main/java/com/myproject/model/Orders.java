package com.myproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList; // Added for list initialization
import java.util.List;      // Added for List type

@Entity
@Table(name = "orders", schema = "nordicsteps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_order_user"))
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "delivery_method", length = 100)
    private String deliveryMethod;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code", foreignKey = @ForeignKey(name = "fk_order_discount"))
    private DiscountCodes discount;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 50)
    private String status;

    @Column(name = "payment_method") // Added this as per earlier discussion for consistency
    private String paymentMethod;


    // --- ADD THIS SECTION ---
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>(); // Initialize the list to prevent NullPointerExceptions
    // --- END ADD SECTION ---

    // You can also add a @PrePersist method if you want to set orderDate automatically on creation
    @PrePersist
    protected void onCreate() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
}