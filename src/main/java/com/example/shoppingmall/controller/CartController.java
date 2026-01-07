package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.AddToCartRequest;
import com.example.shoppingmall.dto.CartResponse;
import com.example.shoppingmall.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Shopping cart management")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get user's cart", description = "Retrieve current user's shopping cart")
    public ResponseEntity<CartResponse> getCart(@RequestAttribute("userId") Long userId) {
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the shopping cart")
    public ResponseEntity<CartResponse> addToCart(
            @RequestAttribute("userId") Long userId,
            @RequestBody AddToCartRequest request) {
        CartResponse cart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the cart")
    public ResponseEntity<Void> removeFromCart(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long cartItemId) {
        cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Remove all items from the cart")
    public ResponseEntity<Void> clearCart(@RequestAttribute("userId") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
