package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ReviewRequest;
import com.example.shoppingmall.dto.ReviewResponse;
import com.example.shoppingmall.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "상품 리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "구매한 상품에 대한 리뷰를 작성합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());
        ReviewResponse response = reviewService.createReview(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "상품 리뷰 목록", description = "특정 상품의 리뷰 목록을 조회합니다.")
    public ResponseEntity<Map<String, Object>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ReviewResponse> reviews = reviewService.getProductReviews(productId, page, size);
        int totalCount = reviewService.getProductReviewCount(productId);
        Double averageRating = reviewService.getProductAverageRating(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews);
        response.put("totalCount", totalCount);
        response.put("averageRating", averageRating);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "내 리뷰 목록", description = "내가 작성한 리뷰 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReviewResponse> reviews = reviewService.getUserReviews(userId);

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());
        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}/stats")
    @Operation(summary = "상품 리뷰 통계", description = "상품의 리뷰 통계 정보를 조회합니다.")
    public ResponseEntity<Map<String, Object>> getReviewStats(@PathVariable Long productId) {
        int reviewCount = reviewService.getProductReviewCount(productId);
        Double averageRating = reviewService.getProductAverageRating(productId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("reviewCount", reviewCount);
        stats.put("averageRating", averageRating);

        return ResponseEntity.ok(stats);
    }
}
