package com.sparta.ordersystem.order.management.Region.service;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Region.dto.RegionCreateRequestDto;
import com.sparta.ordersystem.order.management.Region.dto.RegionCreateResponseDto;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    @Transactional(readOnly = false)
    public RegionCreateResponseDto createRegion(RegionCreateRequestDto regionCreateRequestDto) {

        if(regionRepository.existsByRegionName(regionCreateRequestDto.getRegionName())) {
            throw new IllegalArgumentException("Region name already exists");
        }

        Region region = regionRepository.save(regionCreateRequestDto.toEntity());
        return convertToRegionCreateResponseDto(region);

    }


    private RegionCreateResponseDto convertToRegionCreateResponseDto(Region region) {
        return RegionCreateResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }


}
