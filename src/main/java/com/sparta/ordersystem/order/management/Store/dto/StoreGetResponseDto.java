package com.sparta.ordersystem.order.management.Store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StoreGetResponseDto {
    private UUID storeId;
    private String storeName;
    private Long userId;
    private String userName;
    private boolean isActive;
    private UUID regionId;
    private String regionName;
    private UUID categoryId;
    private String categoryName;


    @Builder
    public StoreGetResponseDto(UUID storeId,
                               String storeName,
                               Long userId,
                               String userName,
                               boolean isActive,
                               UUID regionId,
                               String regionName,
                               UUID categoryId,
                               String categoryName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.userId = userId;
        this.userName = userName;
        this.isActive = isActive;
        this.regionId = regionId;
        this.regionName = regionName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
