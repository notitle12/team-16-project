package com.sparta.ordersystem.order.management.Category.service;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = false)
    public CategoryCreateResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto) {

        if(categoryRepository.existsByCategoryName(categoryCreateRequestDto.getCategoryName())){
            throw new IllegalArgumentException("Category name already exists");
        }


        Category category = categoryRepository.save(categoryCreateRequestDto.toEntity());
        return convertToCategoryCreateResponseDto(category);
    }


    private CategoryCreateResponseDto convertToCategoryCreateResponseDto(Category category) {
        return CategoryCreateResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

}
