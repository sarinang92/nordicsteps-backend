// src/main/java/com/myproject/mapper/OrderMapper.java
package com.myproject.mapper;

import com.myproject.dto.OrderItemResponseDTO;
import com.myproject.dto.OrderRequestDTO;
import com.myproject.dto.OrderResponseDTO;
import com.myproject.model.Orders;
import com.myproject.model.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Use ProductMapper if needed for product details in OrderItemResponseDTO

public interface OrderMapper {

    // --- Mapping OrderItems to OrderItemResponseDTO ---
    @Mapping(source = "orderItemId", target = "orderItemId")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName") // Map product name from linked Products entity
    @Mapping(source = "product.imageUrl", target = "productImageUrl") // Map product image URL
    @Mapping(source = "size", target = "size")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "priceEach", target = "priceEach") // Price at the time of order
    OrderItemResponseDTO toOrderItemResponseDTO(OrderItems orderItem);

    List<OrderItemResponseDTO> toOrderItemResponseDTOs(List<OrderItems> orderItems);


    // --- Mapping OrderRequestDTO to Orders entity (for creating an order) ---
    // Fields handled by backend/auto-generated should be ignored
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by service from authenticated context
    @Mapping(target = "orderDate", ignore = true) // Handled by @PrePersist
    @Mapping(target = "totalAmount", ignore = true) // Calculated by service
    @Mapping(target = "status", ignore = true) // Set by service (e.g., "PENDING", "PAID")
    @Mapping(target = "subtotal", ignore = true) // Calculated by service
    @Mapping(target = "shippingCost", ignore = true) // Calculated by service
    @Mapping(target = "taxAmount", ignore = true) // Calculated by service
    @Mapping(target = "discount", ignore = true) // DiscountCodes entity set by service from discountCode string
    @Mapping(target = "discountAmount", ignore = true) // Calculated by service
    @Mapping(target = "orderItems", ignore = true) // OrderItems are created by service from cart items
    Orders toOrder(OrderRequestDTO orderRequestDTO);


    // --- Mapping Orders entity to OrderResponseDTO (for sending order details to client) ---
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "user.userId", target = "userId") // Map user ID from linked User entity
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "deliveryMethod", target = "deliveryMethod")
    @Mapping(source = "subtotal", target = "subtotal")
    @Mapping(source = "shippingCost", target = "shippingCost")
    @Mapping(source = "discount.code", target = "discountCode") // Map discount code string from DiscountCodes entity
    @Mapping(source = "discountAmount", target = "discountAmount")
    @Mapping(source = "taxAmount", target = "taxAmount")
    @Mapping(source = "orderItems", target = "orderItems") // Map list of OrderItems to OrderItemResponseDTOs
    OrderResponseDTO toOrderResponseDTO(Orders order);

    List<OrderResponseDTO> toOrderResponseDTOs(List<Orders> orders);
}