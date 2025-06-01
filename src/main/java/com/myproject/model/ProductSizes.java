package com.myproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "product_sizes", schema = "nordicsteps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_size_id")
    private Long productSizeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_size_product"))
    private Products product;

    @Column(length = 10, nullable = false)
    private String size;

    @Column(name = "stock_quantity_per_size", nullable = false)
    private Integer stockQuantityPerSize;
}
