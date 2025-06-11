// src/main/java/com/myproject/repository/OrderItemRepository.java
package com.myproject.repository;

import com.myproject.model.OrderItems; // Import the OrderItems entity (plural)
import com.myproject.model.Orders;     // Import the Orders entity if you need to query by it directly
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

    // Find all order items belonging to a specific order
    List<OrderItems> findByOrder(Orders order);

    // Find all order items belonging to a specific order ID
    List<OrderItems> findByOrder_OrderId(Long orderId);

    // You can add more custom query methods here if needed
}