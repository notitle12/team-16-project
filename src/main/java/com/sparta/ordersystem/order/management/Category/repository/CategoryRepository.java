package com.sparta.ordersystem.order.management.Category.repository;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByCategoryNameAndIsActiveTrue(String categoryName);
    List<Category> findAllByIsActiveTrue(Pageable pageable);

}
