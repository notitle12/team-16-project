package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegionDeleteResponseDto {
    private UUID regionId;
    private String regionName;
    private boolean isActive;


    @Builder
    public RegionDeleteResponseDto(UUID regionId, String regionName, boolean isActive) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.isActive = isActive;
    }
}
