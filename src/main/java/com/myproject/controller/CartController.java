package com.myproject.controller;

import com.myproject.dto.CartItemRequestDTO; // New import for request DTO
import com.myproject.dto.CartItemResponseDTO; // New import for response DTO
import com.myproject.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/cart") // Changed base path for better organization
@Tag(name = "Cart Controller", description = "API for managing items in the shopping cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    // Placeholder for user ID. In a real application, this would come from authentication context.
    private final Long TEMP_USER_ID = 1L;

    @PostMapping("/items") // Specific endpoint for adding items
    @Operation(summary = "Add an item to the cart")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid cart item data"),
            @ApiResponse(responseCode = "404", description = "Product or Cart not found")
    })
    public ResponseEntity<CartItemResponseDTO> addItemToCart(@Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO addedItem = cartItemService.addItemToCart(TEMP_USER_ID, cartItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

    @GetMapping("/items") // Specific endpoint for getting all items
    @Operation(summary = "Get all items in the current user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CartItemResponseDTO>> getAllCartItems() {
        List<CartItemResponseDTO> cartItems = cartItemService.getAllCartItems(TEMP_USER_ID);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/items/{id}") // Specific endpoint for getting a single item
    @Operation(summary = "Get a cart item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart item"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartItemResponseDTO> getCartItemById(@PathVariable Long id) {
        CartItemResponseDTO cartItemDTO = cartItemService.getCartItemById(id);
        return ResponseEntity.ok(cartItemDTO);
    }

    @PutMapping("/items/{id}") // Specific endpoint for updating quantity
    @Operation(summary = "Update an item's quantity in the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity provided"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartItemResponseDTO> updateCartItemQuantity(
            @PathVariable Long id,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        CartItemResponseDTO updatedItemDTO = cartItemService.updateCartItemQuantity(id, quantity);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @DeleteMapping("/items/{id}") // Specific endpoint for removing an item
    @Operation(summary = "Remove an item from the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        cartItemService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear") // Specific endpoint for clearing the cart
    @Operation(summary = "Clear the entire cart for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> clearCart() {
        cartItemService.clearCart(TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }
}