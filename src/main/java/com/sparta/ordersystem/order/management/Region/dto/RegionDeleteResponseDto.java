package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RegionDeleteResponseDto {


    private final UUID regionId;
    private final String regionName;
    private final boolean isActive;
    private final LocalDateTime deletedAt;
    private final Long deletedBy;

    @Builder
    public RegionDeleteResponseDto(UUID regionId, String regionName, boolean isActive, LocalDateTime deletedAt, Long deletedBy) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.isActive = isActive;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

}
