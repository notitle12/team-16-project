package com.sparta.ordersystem.order.management.Store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
public class StoreCreateResponseDto {

    private final UUID storeId;
    private final String storeName;
    private final String categoryName;
    private final String regionName;

    @Builder
    public StoreCreateResponseDto(UUID storeId, String storeName, String categoryName, String regionName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.categoryName = categoryName;
        this.regionName = regionName;
    }
}