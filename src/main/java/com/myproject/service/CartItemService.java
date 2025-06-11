package com.myproject.service;

import com.myproject.dto.CartItemRequestDTO;
import com.myproject.dto.CartItemResponseDTO;
import com.myproject.mapper.CartItemMapper;
import com.myproject.model.Cart; // Import the Cart entity
import com.myproject.model.CartItems;
import com.myproject.model.Products; // Use Products (plural)
import com.myproject.model.User; // Import User entity if managing cart per user directly
import com.myproject.repository.CartItemRepository;
import com.myproject.repository.CartRepository; // Assuming you have this repository
import com.myproject.repository.ProductRepository; // Still named ProductRepository, consider renaming to ProductsRepository
import com.myproject.repository.UserRepository; // Assuming you have this repository for user management
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductRepository productRepository; // Using your provided ProductRepository (should be for Products entity)
    private final CartRepository cartRepository; // Inject CartRepository
    private final UserRepository userRepository; // Inject UserRepository for user-specific cart management

    /**
     * Retrieves the active cart for a given user ID, or creates a new one if it doesn't exist.
     * This method assumes user authentication is handled elsewhere and provides the userId.
     * In a production application, you might get the userId from Spring Security context.
     *
     * @param userId The ID of the user.
     * @return The active Cart for the user.
     * @throws EntityNotFoundException if the user does not exist when trying to create a new cart.
     */
    private Cart getOrCreateActiveCart(Long userId) {
        // Find an active cart for the user
        Optional<Cart> existingCart = cartRepository.findByUser_UserIdAndStatus(userId, "active");

        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            // Fetch the User entity
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            // Create a new cart for the user
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus("active");
            newCart.setCreatedAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        }
    }

    /**
     * Adds a product to the user's cart or updates its quantity if it already exists.
     * Handles fetching product, finding/creating cart, and calculating total price.
     *
     * @param userId The ID of the user whose cart is being modified.
     * @param cartItemRequestDTO DTO containing product ID, quantity, and size.
     * @return CartItemResponseDTO representing the added or updated cart item.
     * @throws EntityNotFoundException if the product or user is not found.
     * @throws IllegalArgumentException if quantity is less than 1.
     */
    public CartItemResponseDTO addItemToCart(Long userId, CartItemRequestDTO cartItemRequestDTO) {
        if (cartItemRequestDTO.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        // 1. Get or Create Cart for the user
        Cart userCart = getOrCreateActiveCart(userId);

        // 2. Fetch the Product
        Products product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

        // 3. Check if the item (same product and size) already exists in this specific cart
        Optional<CartItems> existingCartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductIdAndSize(
                userCart.getCartId(), product.getProductId(), cartItemRequestDTO.getSize());

        CartItems cartItem;
        if (existingCartItem.isPresent()) {
            // Update quantity and total price
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
        } else {
            // Create new cart item
            cartItem = new CartItems(); // Manually instantiate to set relationships
            cartItem.setCart(userCart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItem.setSize(cartItemRequestDTO.getSize());
        }

        // Calculate total price for the item
        BigDecimal itemTotalPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cartItem.setTotalPrice(itemTotalPrice);

        CartItems savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponseDTO(savedCartItem);
    }

    /**
     * Retrieves all cart items for a specific user's active cart.
     *
     * @param userId The ID of the user.
     * @return A list of CartItemResponseDTOs.
     * @throws EntityNotFoundException if the user's cart is not found.
     */
    public List<CartItemResponseDTO> getAllCartItems(Long userId) {
        Cart userCart = getOrCreateActiveCart(userId);
        List<CartItems> cartItems = cartItemRepository.findByCart_CartId(userCart.getCartId());
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single cart item by its ID.
     *
     * @param id The ID of the cart item.
     * @return CartItemResponseDTO if found.
     * @throws EntityNotFoundException if the cart item is not found.
     */
    public CartItemResponseDTO getCartItemById(Long id) {
        CartItems cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + id));
        return cartItemMapper.toCartItemResponseDTO(cartItem);
    }

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param cartItemId The ID of the cart item to update.
     * @param quantity The new quantity for the item.
     * @return CartItemResponseDTO representing the updated cart item.
     * @throws EntityNotFoundException if the cart item is not found.
     * @throws IllegalArgumentException if quantity is less than 1.
     */
    public CartItemResponseDTO updateCartItemQuantity(Long cartItemId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        CartItems cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + cartItemId));

        cartItem.setQuantity(quantity);
        // Recalculate total price based on updated quantity and current product price
        BigDecimal itemTotalPrice = cartItem.getProduct().getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
        cartItem.setTotalPrice(itemTotalPrice);

        CartItems updatedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponseDTO(updatedCartItem);
    }

    /**
     * Removes a single cart item from the cart by its ID.
     *
     * @param id The ID of the cart item to remove.
     * @throws EntityNotFoundException if the cart item is not found.
     */
    public void removeCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Cart item not found with ID: " + id);
        }
        cartItemRepository.deleteById(id);
    }

    /**
     * Clears all items from a specific user's active cart.
     *
     * @param userId The ID of the user whose cart should be cleared.
     * @throws EntityNotFoundException if the user's cart is not found.
     */
    public void clearCart(Long userId) {
        Cart userCart = getOrCreateActiveCart(userId);
        List<CartItems> itemsToClear = cartItemRepository.findByCart_CartId(userCart.getCartId());
        cartItemRepository.deleteAll(itemsToClear);
    }
}