package com.sparta.ordersystem.order.management.Category.dto;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequestDto {

    @NotBlank(message = "categoryName cannot be blank")
    private String categoryName;  // 필드 이름을 엔티티와 일치시킴

    public CategoryUpdateRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

}
