package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryCreateResponseDto {

    private final UUID categoryId;      // 스네이크 케이스로 변경
    private final String categoryName;  // 스네이크 케이스로 변경

    @Builder
    public CategoryCreateResponseDto(UUID categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
