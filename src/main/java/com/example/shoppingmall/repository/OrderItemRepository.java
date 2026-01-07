package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.OrderItem;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}
