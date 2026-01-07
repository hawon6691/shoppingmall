package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Order;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY order_date DESC")
    List<Order> findByUserId(@Param("userId") Long userId);

    @Query("SELECT * FROM orders WHERE order_number = :orderNumber")
    Optional<Order> findByOrderNumber(@Param("orderNumber") String orderNumber);
}
