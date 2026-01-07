package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Review;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query("SELECT * FROM reviews WHERE product_id = :productId ORDER BY created_at DESC")
    List<Review> findByProductId(@Param("productId") Long productId);

    @Query("SELECT * FROM reviews WHERE user_id = :userId ORDER BY created_at DESC")
    List<Review> findByUserId(@Param("userId") Long userId);

    @Query("SELECT * FROM reviews WHERE product_id = :productId ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<Review> findByProductIdWithPagination(
            @Param("productId") Long productId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query("SELECT COUNT(*) FROM reviews WHERE product_id = :productId")
    int countByProductId(@Param("productId") Long productId);

    @Query("SELECT AVG(rating) FROM reviews WHERE product_id = :productId")
    Double getAverageRating(@Param("productId") Long productId);

    @Query("SELECT * FROM reviews WHERE user_id = :userId AND product_id = :productId AND order_id = :orderId")
    Optional<Review> findByUserIdAndProductIdAndOrderId(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("orderId") Long orderId
    );
}
