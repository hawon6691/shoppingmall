package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.Order;
import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.domain.Review;
import com.example.shoppingmall.domain.User;
import com.example.shoppingmall.dto.ReviewRequest;
import com.example.shoppingmall.dto.ReviewResponse;
import com.example.shoppingmall.exception.DuplicateReviewException;
import com.example.shoppingmall.exception.ResourceNotFoundException;
import com.example.shoppingmall.exception.UnauthorizedException;
import com.example.shoppingmall.repository.OrderRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.ReviewRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        // Verify product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify order exists and belongs to user
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only review products from your own orders");
        }

        // Check if review already exists for this user-product-order combination
        reviewRepository.findByUserIdAndProductIdAndOrderId(userId, request.getProductId(), request.getOrderId())
                .ifPresent(r -> {
                    throw new DuplicateReviewException("You have already reviewed this product for this order");
                });

        // Create review
        Review review = Review.builder()
                .productId(request.getProductId())
                .userId(userId)
                .orderId(request.getOrderId())
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        return convertToResponse(savedReview, product.getName(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getProductReviews(Long productId, int page, int size) {
        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int offset = (page - 1) * size;
        List<Review> reviews = reviewRepository.findByProductIdWithPagination(productId, size, offset);

        return reviews.stream()
                .map(review -> {
                    User user = userRepository.findById(review.getUserId()).orElse(null);
                    Product product = productRepository.findById(review.getProductId()).orElse(null);
                    return convertToResponse(
                            review,
                            product != null ? product.getName() : "Unknown",
                            user != null ? user.getUsername() : "Unknown"
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(review -> {
                    User user = userRepository.findById(review.getUserId()).orElse(null);
                    Product product = productRepository.findById(review.getProductId()).orElse(null);
                    return convertToResponse(
                            review,
                            product != null ? product.getName() : "Unknown",
                            user != null ? user.getUsername() : "Unknown"
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double getProductAverageRating(Long productId) {
        Double avgRating = reviewRepository.getAverageRating(productId);
        return avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0.0;
    }

    @Transactional(readOnly = true)
    public int getProductReviewCount(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own reviews");
        }

        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponse convertToResponse(Review review, String productName, String username) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .productName(productName)
                .userId(review.getUserId())
                .username(username)
                .orderId(review.getOrderId())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
