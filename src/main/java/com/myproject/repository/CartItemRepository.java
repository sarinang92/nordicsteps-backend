package com.myproject.repository;

import com.myproject.model.CartItems; // Changed to CartItems (plural)
import com.myproject.model.Products; // Import Products entity
import com.myproject.model.Cart; // Import Cart entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> { // Changed to CartItems (plural)
    // Find a cart item by its associated Cart ID, Product ID, and Size
    Optional<CartItems> findByCart_CartIdAndProduct_ProductIdAndSize(Long cartId, Long productId, String size);

    // Find all cart items belonging to a specific Cart ID
    List<CartItems> findByCart_CartId(Long cartId);
}