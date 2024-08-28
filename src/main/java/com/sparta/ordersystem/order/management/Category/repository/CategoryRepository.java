package com.sparta.ordersystem.order.management.Category.repository;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByCategoryName(String categoryName);
}
