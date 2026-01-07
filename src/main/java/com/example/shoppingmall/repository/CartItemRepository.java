package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.CartItem;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    @Query("SELECT * FROM cart_items WHERE cart_id = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    @Query("SELECT * FROM cart_items WHERE cart_id = :cartId AND product_id = :productId")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM cart_items WHERE cart_id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);
}
