// src/main/java/com/myproject/repository/OrderRepository.java
package com.myproject.repository;

import com.myproject.model.Orders; // Import the Orders entity (plural)
import com.myproject.model.User;   // Import the User entity if you need to query by it directly
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    // Find all orders for a specific user
    List<Orders> findByUser(User user);

    // Find all orders for a specific user ID
    List<Orders> findByUser_UserId(Long userId);

    // Find orders by status
    List<Orders> findByStatus(String status);

    // You can add more custom query methods here as needed,
    // e.g., find by order date range, find by total amount range, etc.
}