package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Category;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query("SELECT * FROM categories WHERE parent_id IS NULL")
    List<Category> findRootCategories();

    @Query("SELECT * FROM categories WHERE parent_id = :parentId")
    List<Category> findByParentId(@Param("parentId") Long parentId);
}
