// src/main/java/com/myproject/repository/CartItemRepository.java
package com.myproject.repository;

import com.myproject.model.CartItems;
import com.myproject.model.Cart; // Import the Cart entity
import com.myproject.model.Products; // Import Products entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    // Find all cart items belonging to a specific Cart entity
    List<CartItems> findByCart(Cart cart);

    // Find all cart items belonging to a specific Cart ID (THIS IS THE MISSING METHOD)
    List<CartItems> findByCart_CartId(Long cartId); // <--- ADD OR ENSURE THIS LINE IS PRESENT

    // Find a cart item by its associated Cart ID, Product ID, and Size
    Optional<CartItems> findByCart_CartIdAndProduct_ProductIdAndSize(Long cartId, Long productId, String size);

    // You might add other methods here if needed, e.g., to delete items by cart
    void deleteByCart(Cart cart);
}