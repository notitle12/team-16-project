package com.sparta.ordersystem.order.management.Store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StoreUpdateResponseDto {

    private UUID storeId;
    private String storeName;
    private boolean isActive;
    private UUID regionId;
    private UUID categoryId;

    @Builder
    public StoreUpdateResponseDto(UUID storeId, String storeName, boolean isActive, UUID regionId, UUID categoryId){
        this.storeId = storeId;
        this.storeName = storeName;
        this.isActive = isActive;
        this.regionId = regionId;
        this.categoryId = categoryId;
    }

}
