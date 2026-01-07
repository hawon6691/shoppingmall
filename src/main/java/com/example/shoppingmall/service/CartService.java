package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.Cart;
import com.example.shoppingmall.domain.CartItem;
import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.dto.AddToCartRequest;
import com.example.shoppingmall.dto.CartItemResponse;
import com.example.shoppingmall.dto.CartResponse;
import com.example.shoppingmall.exception.InsufficientStockException;
import com.example.shoppingmall.exception.ResourceNotFoundException;
import com.example.shoppingmall.repository.CartItemRepository;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().userId(userId).build();
                    return cartRepository.save(newCart);
                });

        // Validate product and stock
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        // Check if item already in cart
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElse(null);

        if (cartItem != null) {
            // Update quantity
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            // Add new item
            CartItem newItem = CartItem.builder()
                    .cartId(cart.getId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
        }

        return getCart(userId);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .build();
    }

    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCartId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item does not belong to user's cart");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .imageUrl(product.getImageUrl())
                .build();
    }
}
