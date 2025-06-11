// src/main/java/com/myproject/repository/CartRepository.java
package com.myproject.repository;

import com.myproject.model.Cart;
import com.myproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Find a cart by user and status (e.g., "active")
    Optional<Cart> findByUser_UserIdAndStatus(Long userId, String status);

    // Find a cart by the User entity object and status (THIS IS THE NEW/CORRECTED METHOD)
    Optional<Cart> findByUserAndStatus(User user, String status);

    // You might add other methods here, e.g., findByUser(User user)
    Optional<Cart> findByUser(User user);
}