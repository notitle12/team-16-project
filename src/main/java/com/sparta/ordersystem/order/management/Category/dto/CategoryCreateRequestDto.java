package com.sparta.ordersystem.order.management.Category.dto;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequestDto {

    private String categoryName;  // 필드 이름을 엔티티와 일치시킴

    public CategoryCreateRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category toEntity() {
        return Category.builder()
                .categoryName(this.categoryName) // 필드 이름 일치
                .build();
    }

}