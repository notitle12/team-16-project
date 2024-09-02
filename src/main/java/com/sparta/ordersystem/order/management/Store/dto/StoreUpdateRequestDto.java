package com.sparta.ordersystem.order.management.Store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StoreUpdateRequestDto {
    private String storeName;
    private UUID regionId;
    private UUID categoryId;

    @Builder
    public StoreUpdateRequestDto(String storeName, UUID regionId, UUID categoryId){
        this.storeName = storeName;
        this.regionId = regionId;
        this.categoryId = categoryId;
    }
}
