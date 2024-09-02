package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CategoryUpdateResponseDto {
    private final UUID categoryId;
    private final String categoryName;
    private final LocalDateTime updatedAt;
    private final Long updatedBy;

    @Builder
    public CategoryUpdateResponseDto(UUID categoryId, String categoryName, LocalDateTime updatedAt, Long updatedBy) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

}
