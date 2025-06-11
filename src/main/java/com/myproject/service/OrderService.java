// src/main/java/com/myproject/service/OrderService.java
package com.myproject.service;

import com.myproject.dto.OrderRequestDTO;
import com.myproject.dto.OrderResponseDTO;
import com.myproject.mapper.OrderMapper;
import com.myproject.model.Cart;
import com.myproject.model.CartItems;
import com.myproject.model.Orders;
import com.myproject.model.OrderItems;
import com.myproject.model.Products;
import com.myproject.model.User;
// import com.myproject.model.DiscountCodes; // Removed import

import com.myproject.repository.CartRepository;
import com.myproject.repository.CartItemRepository;
import com.myproject.repository.OrderRepository;
import com.myproject.repository.OrderItemRepository;
import com.myproject.repository.ProductRepository;
import com.myproject.repository.UserRepository;
// import com.myproject.repository.DiscountCodeRepository; // Removed import

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
// import java.time.LocalDate; // Removed import
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional; // Removed import

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    // private final DiscountCodeRepository discountCodeRepository; // Removed
    private final OrderMapper orderMapper;

    /**
     * Places a new order for a user based on their active shopping cart.
     *
     * @param userId The ID of the user placing the order.
     * @param requestDTO The OrderRequestDTO containing shipping, payment, and delivery details.
     * @return OrderResponseDTO representing the newly placed order.
     * @throws EntityNotFoundException if user or cart not found.
     * @throws IllegalArgumentException if cart is empty or stock is insufficient.
     */
    public OrderResponseDTO placeOrder(Long userId, OrderRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUserAndStatus(user, "active")
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found for user ID: " + userId));

        List<CartItems> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot place order: Shopping cart is empty.");
        }

        // --- 1. Initialize Order and Calculate Subtotal ---
        Orders order = orderMapper.toOrder(requestDTO);
        order.setUser(user);
        order.setStatus("PENDING");

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItems> orderItems = new ArrayList<>();

        // --- 2. Process Cart Items, Check Stock, and Capture Price ---
        for (CartItems cartItem : cartItems) {
            Products product = cartItem.getProduct();
            Integer requestedQuantity = cartItem.getQuantity();

            if (product.getTotalStockQuantity() < requestedQuantity) {
                throw new IllegalArgumentException(
                    "Insufficient stock for product: " + product.getName() + " (" + product.getProductId() + "). " +
                    "Available: " + product.getTotalStockQuantity() + ", Requested: " + requestedQuantity
                );
            }

            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQuantity);
            orderItem.setSize(cartItem.getSize());
            orderItem.setPriceEach(product.getCurrentPrice());

            orderItems.add(orderItem);
            subtotal = subtotal.add(product.getCurrentPrice().multiply(BigDecimal.valueOf(requestedQuantity)));

            product.setTotalStockQuantity(product.getTotalStockQuantity() - requestedQuantity);
            productRepository.save(product);
        }

        order.setSubtotal(subtotal);
        order.setOrderItems(orderItems);

        // --- 3. Discount Code Logic Removed ---
        // As per instruction, discount code handling is ignored for now.
        order.setDiscount(null); // Ensure no discount entity is linked
        order.setDiscountAmount(BigDecimal.ZERO); // Ensure discount amount is zero

        // --- 4. Calculate Shipping Cost (Placeholder) ---
        BigDecimal shippingCost = calculateShippingCost(requestDTO.getDeliveryMethod(), subtotal);
        order.setShippingCost(shippingCost);

        // --- 5. Calculate Tax Amount (Placeholder) ---
        BigDecimal taxAmount = calculateTaxAmount(subtotal, shippingCost, BigDecimal.ZERO, order.getShippingAddress()); // discountAmount is now zero
        order.setTaxAmount(taxAmount);

        // --- 6. Calculate Final Total Amount ---
        BigDecimal totalAmount = subtotal
                                .subtract(BigDecimal.ZERO) // Discount is zero
                                .add(shippingCost)
                                .add(taxAmount);
        order.setTotalAmount(totalAmount);

        // --- 7. Save Order and Order Items ---
        Orders savedOrder = orderRepository.save(order);
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);

        // --- 8. Clear the User's Cart ---
        cart.setStatus("ordered");
        cartRepository.save(cart);

        // --- 9. Simulate Payment (if applicable) ---
        order.setStatus("PAID");
        orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    /**
     * Retrieves all orders for a specific user.
     * @param userId The ID of the user.
     * @return A list of OrderResponseDTOs.
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        List<Orders> orders = orderRepository.findByUser(user);
        return orderMapper.toOrderResponseDTOs(orders);
    }

    /**
     * Retrieves a single order by its ID.
     * @param orderId The ID of the order.
     * @return OrderResponseDTO.
     */
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        return orderMapper.toOrderResponseDTO(order);
    }

    /**
     * Updates the status of an order.
     * @param orderId The ID of the order to update.
     * @param newStatus The new status (e.g., "SHIPPED", "DELIVERED").
     * @return The updated OrderResponseDTO.
     */
    public OrderResponseDTO updateOrderStatus(Long orderId, String newStatus) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);
        Orders updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(updatedOrder);
    }

    // --- Placeholder / Dummy Calculation Methods ---
    private BigDecimal calculateShippingCost(String deliveryMethod, BigDecimal subtotal) {
        if ("Express".equalsIgnoreCase(deliveryMethod)) {
            return new BigDecimal("15.00");
        } else if ("Standard".equalsIgnoreCase(deliveryMethod)) {
            return new BigDecimal("10.00");
        } else if ("Pickup".equalsIgnoreCase(deliveryMethod)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal("10.00");
    }

    private BigDecimal calculateTaxAmount(BigDecimal subtotal, BigDecimal shippingCost, BigDecimal discountAmount, String shippingAddress) {
        BigDecimal taxableAmount = subtotal.subtract(discountAmount).add(shippingCost);
        return taxableAmount.multiply(new BigDecimal("0.25"));
    }
}