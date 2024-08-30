package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegionCreateResponseDto {

    private final UUID regionId;
    private final String regionName;

    @Builder
    public RegionCreateResponseDto(UUID regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }
}
