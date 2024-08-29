package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryDeleteResponseDto {


    private final UUID categoryId;
    private final String categoryName;
    private final boolean isActive;

    @Builder
    public CategoryDeleteResponseDto(UUID categoryId, String categoryName, boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
    }

}
