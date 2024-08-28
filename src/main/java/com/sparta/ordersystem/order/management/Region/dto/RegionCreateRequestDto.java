package com.sparta.ordersystem.order.management.Region.dto;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionCreateRequestDto {

    private String regionName;

    public RegionCreateRequestDto(String regionName) {
        this.regionName = regionName;
    }

    public Region toEntity() {
        return Region.builder()
                .regionName(this.regionName)
                .build();
    }

}