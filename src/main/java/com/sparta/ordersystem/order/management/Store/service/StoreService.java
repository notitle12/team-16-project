package com.sparta.ordersystem.order.management.Store.service;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Category.repository.CategoryRepository;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import com.sparta.ordersystem.order.management.Store.dto.StoreCreateRequestDto;
import com.sparta.ordersystem.order.management.Store.dto.StoreCreateResponseDto;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.Store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;

    @Transactional(readOnly = false)
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Region region = regionRepository.findById(requestDto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid region ID"));

        Store store = requestDto.toEntity(category, region);
        storeRepository.save(store);

        return convertToStoreCreateResponseDto(store, category, region);
    }



    // Entity -> DTO
    private StoreCreateResponseDto convertToStoreCreateResponseDto(Store store, Category category, Region region) {
        return StoreCreateResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .categoryName(category.getCategoryName())
                .regionName(region.getRegionName())
                .build();
    }
}
