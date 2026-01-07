package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Cart;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query("SELECT * FROM carts WHERE user_id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);
}
