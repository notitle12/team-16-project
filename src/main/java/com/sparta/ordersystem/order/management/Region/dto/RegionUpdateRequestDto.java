package com.sparta.ordersystem.order.management.Region.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionUpdateRequestDto {
    @NotBlank(message = "지역명을 반드시 입력해주세요.")
    private String regionName;

    public RegionUpdateRequestDto(String regionName) {
        this.regionName = regionName;
    }
}
