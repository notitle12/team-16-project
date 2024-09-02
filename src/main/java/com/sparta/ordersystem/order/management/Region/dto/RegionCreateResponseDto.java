package com.sparta.ordersystem.order.management.Region.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RegionCreateResponseDto {

    private final UUID regionId;      // 스네이크 케이스로 변경
    private final String regionName;  // 스네이크 케이스로 변경
    private final LocalDateTime createdAt; // 생성 날짜
    private final Long createdBy; // 생성한 사람

    @Builder
    public RegionCreateResponseDto(UUID regionId, String regionName, LocalDateTime createdAt, Long createdBy) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
