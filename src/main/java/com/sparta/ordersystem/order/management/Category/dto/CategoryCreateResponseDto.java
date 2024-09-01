package com.sparta.ordersystem.order.management.Category.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CategoryCreateResponseDto {

    private final UUID categoryId;      // 스네이크 케이스로 변경
    private final String categoryName;  // 스네이크 케이스로 변경
    private final LocalDateTime createdAt; // 생성 날짜
    private final Long createdBy; // 생성한 사람

    @Builder
    public CategoryCreateResponseDto(UUID categoryId, String categoryName, LocalDateTime createdAt, Long createdBy) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
