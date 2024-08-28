package com.sparta.ordersystem.order.management.Store.dto;


import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.User.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class StoreCreateRequestDto {
    private String storeName;
    private UUID categoryId;
    private UUID regionId;


    @Builder
    public StoreCreateRequestDto(String storeName, UUID categoryId, UUID regionId) {
        this.storeName = storeName;
        this.categoryId = categoryId;
        this.regionId = regionId;
    }

    // toEntity 메서드 추가
    public Store toEntity(Category category, Region region, User user) {
        return Store.builder()
                .storeName(this.storeName)
                .category(category)
                .region(region)
                .user(user)
                .build();
    }
}
