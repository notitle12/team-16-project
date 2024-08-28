package com.sparta.ordersystem.order.management.Region.dto;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionCreateRequestDto {


    @NotBlank(message = "regionName cannot be blank")
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