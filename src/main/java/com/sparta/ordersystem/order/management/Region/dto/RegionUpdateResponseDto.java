package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RegionUpdateResponseDto {
    private final UUID regionId;
    private final String regionName;
    private final LocalDateTime updatedAt;
    private final Long updatedBy;

    @Builder
    public RegionUpdateResponseDto(UUID regionId, String regionName, LocalDateTime updatedAt, Long updatedBy) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

}
