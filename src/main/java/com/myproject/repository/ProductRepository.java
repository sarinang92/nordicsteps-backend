package com.myproject.repository;

import com.myproject.model.Products; // Changed to Products (plural)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Import BigDecimal for price
import java.util.List;
import java.util.Optional; // Usually good to include for findById etc.

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> { // Changed to Products (plural)
    // Find products by name (case-insensitive and containing)
    List<Products> findByNameContainingIgnoreCase(String name);

    // Find products where currentPrice is greater than a specified value
    // Assuming your Products entity has a 'currentPrice' field, not 'price'
    List<Products> findByCurrentPriceGreaterThan(BigDecimal price);

    // Add any other custom query methods here that work with the Products entity
}