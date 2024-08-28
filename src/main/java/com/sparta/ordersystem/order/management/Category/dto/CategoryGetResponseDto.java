package com.sparta.ordersystem.order.management.Category.dto;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CategoryGetResponseDto {
    private UUID categoryId;
    private String categoryName;

    @Builder
    public CategoryGetResponseDto(UUID categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
