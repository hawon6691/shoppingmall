package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT * FROM products WHERE is_active = true")
    List<Product> findAllActive();

    @Query("SELECT * FROM products WHERE is_active = true ORDER BY id DESC LIMIT :limit OFFSET :offset")
    List<Product> findAllActiveWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT COUNT(*) FROM products WHERE is_active = true")
    int countAllActive();

    @Query("SELECT * FROM products WHERE category_id = :categoryId AND is_active = true")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT * FROM products WHERE name LIKE CONCAT('%', :keyword, '%') AND is_active = true")
    List<Product> searchByName(@Param("keyword") String keyword);
}
