package com.sparta.ordersystem.order.management.Region.service;

import com.sparta.ordersystem.order.management.Region.dto.RegionCreateRequestDto;
import com.sparta.ordersystem.order.management.Region.dto.RegionCreateResponseDto;
import com.sparta.ordersystem.order.management.Region.dto.RegionGetResponseDto;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public RegionGetResponseDto getRegion(UUID region_id) {
        Region region = regionRepository.findById(region_id).orElseThrow(
                ()-> new IllegalArgumentException("Region not found")
        );

        return convertToRegionGetResponseDto(region);
    }

    public List<RegionGetResponseDto> getAllRegion(int page,
                                                   int size,
                                                   String sortBy,
                                                   boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        return regionRepository.findAllByIsActiveTrue(pageable).stream()
                .map(this::convertToRegionGetResponseDto)
                .toList();
    }







    private RegionCreateResponseDto convertToRegionCreateResponseDto(Region region) {
        return RegionCreateResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }

    private RegionGetResponseDto convertToRegionGetResponseDto(Region region) {
        return RegionGetResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }



}
