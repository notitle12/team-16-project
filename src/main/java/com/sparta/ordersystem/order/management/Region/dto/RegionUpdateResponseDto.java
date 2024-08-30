package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegionUpdateResponseDto {

    private UUID regionId;
    private String regionName;

    @Builder
    public RegionUpdateResponseDto(UUID regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }
}
