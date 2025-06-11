// src/main/java/com/myproject/service/CartItemService.java
package com.myproject.service;

import com.myproject.dto.CartItemRequestDTO;
import com.myproject.dto.CartItemResponseDTO;
import com.myproject.mapper.CartItemMapper;
import com.myproject.model.CartItems; // Ensure this is your actual CartItem entity class name
import com.myproject.model.Products; // Assuming your Product entity is named Products
import com.myproject.repository.CartItemRepository;
import com.myproject.repository.ProductRepository; // New import
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductRepository productRepository; // Inject ProductRepository

    // Add item to cart and return CartItemResponseDTO
    public CartItemResponseDTO addItemToCart(CartItemRequestDTO cartItemRequestDTO) {
        // Fetch the Product entity
        Products product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

        // Check if a cart item with the same product and size already exists for the current user's cart
        // (Assuming you have a way to determine the current user's cart or a default cart logic)
        // For simplicity, let's assume we always create a new one or update if found based on product and size
        // If you have a user/cart context, you'd find an existing item here.
        CartItems existingCartItem = cartItemRepository.findByProductAndSize(product, cartItemRequestDTO.getSize()) // You'd need to add this method to your CartItemRepository
                .orElse(null);

        CartItems cartItem;
        if (existingCartItem != null) {
            // Update quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequestDTO.getQuantity());
            cartItem = existingCartItem;
        } else {
            // Create a new cart item
            cartItem = cartItemMapper.toCartItem(cartItemRequestDTO); // Maps quantity and size
            cartItem.setProduct(product); // Set the fetched Product entity
            // Assume you also have a way to link this to a Cart entity (e.g., based on current user's active cart)
            // cartItem.setCart(currentUserCart);
        }

        // Calculate total price for the item
        BigDecimal itemTotalPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cartItem.setTotalPrice(itemTotalPrice);

        CartItems savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponseDTO(savedCartItem);
    }

    // Get all cart items and map to DTOs
    public List<CartItemResponseDTO> getAllCartItems() {
        List<CartItems> cartItems = cartItemRepository.findAll();
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponseDTO)
                .collect(Collectors.toList());
    }

    // Get cart item by ID and map to DTO
    public CartItemResponseDTO getCartItemById(Long id) {
        CartItems cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        return cartItemMapper.toCartItemResponseDTO(cartItem);
    }

    // Update cart item quantity and return updated CartItemResponseDTO
    // This method now handles quantity AND size update if needed, but primarily quantity
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO cartItemRequestDTO) {
        CartItems cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        // Only update quantity and size from the request DTO
        // The mapper's update method can handle this
        cartItemMapper.updateCartItemFromDto(cartItemRequestDTO, cartItem);

        // Fetch the product again in case the product ID changed (though typically not for update quantity)
        // Or ensure the product is already loaded via the initial cartItem fetch if it's eagerly loaded,
        // or fetch if lazily loaded and accessed.
        Products product = cartItem.getProduct();
        if (product == null || !product.getProductId().equals(cartItemRequestDTO.getProductId())) {
            // This case implies product ID in request DTO is different, implying a change of product for the item
            // This might need a more complex logic, but for simple quantity/size update, productId should match.
            // If productId can change during update, fetch new product.
            product = productRepository.findById(cartItemRequestDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + cartItemRequestDTO.getProductId()));
            cartItem.setProduct(product);
        }


        // Recalculate total price
        BigDecimal itemTotalPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cartItem.setTotalPrice(itemTotalPrice);

        CartItems updatedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponseDTO(updatedCartItem);
    }


    // Remove cart item by ID
    public void removeCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Cart item not found");
        }
        cartItemRepository.deleteById(id);
    }

    // Clear all cart items
    public void clearCart() {
        cartItemRepository.deleteAll();
    }
}