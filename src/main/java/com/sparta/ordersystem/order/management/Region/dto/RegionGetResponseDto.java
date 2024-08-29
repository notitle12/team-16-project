package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegionGetResponseDto {
    private UUID regionId;
    private String regionName;

    @Builder
    public RegionGetResponseDto(UUID regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }
}
