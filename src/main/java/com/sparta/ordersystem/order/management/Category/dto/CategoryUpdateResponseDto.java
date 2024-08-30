package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryUpdateResponseDto {
    private final UUID categoryId;
    private final String categoryName;

    @Builder
    public CategoryUpdateResponseDto(UUID categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
