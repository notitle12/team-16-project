package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CategoryDeleteResponseDto {


    private final UUID categoryId;
    private final String categoryName;
    private final boolean isActive;
    private final LocalDateTime deletedAt;
    private final Long deletedBy;

    @Builder
    public CategoryDeleteResponseDto(UUID categoryId, String categoryName, boolean isActive, LocalDateTime deletedAt, Long deletedBy) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

}
