package com.sparta.ordersystem.order.management.Store.service;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Category.repository.CategoryRepository;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import com.sparta.ordersystem.order.management.Store.dto.StoreCreateRequestDto;
import com.sparta.ordersystem.order.management.Store.dto.StoreCreateResponseDto;
import com.sparta.ordersystem.order.management.Store.dto.StoreGetResponseDto;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.Store.repository.StoreRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
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
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;

    @Transactional(readOnly = false)
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto, User user) {

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Region region = regionRepository.findById(requestDto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid region ID"));

        Store store = requestDto.toEntity(category, region, user);
        storeRepository.save(store);

        return convertToStoreCreateResponseDto(store, category, region, user);
    }


    @Transactional(readOnly = true)
    public StoreGetResponseDto getStore(UUID storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                ()-> new IllegalArgumentException("잘못된 가게 id 입니다.")
        );

        return convertToStoreGetResponseDto(store);
    }

    @Transactional(readOnly = true)
    public List<StoreGetResponseDto> getAllStore(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);


        return storeRepository.findAllByIsActiveTrue(pageable).stream()
                .map(this::convertToStoreGetResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StoreGetResponseDto> getAllStoreByUser(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        return storeRepository.findAllByUserIdAndIsActive(user.getUser_id(), pageable).stream()
                .map(this::convertToStoreGetResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StoreGetResponseDto> getAllStoreByRegion(UUID regionId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        return storeRepository.findAllByRegion_RegionIdAndIsActiveTrue(regionId, pageable).stream()
                .map(this::convertToStoreGetResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StoreGetResponseDto> getAllStoreByRegionAndCategory(UUID regionId, UUID categoryId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        return storeRepository.findAllByRegion_RegionIdAndCategory_CategoryIdAndIsActiveTrue(regionId, categoryId, pageable).stream()
                .map(this::convertToStoreGetResponseDto)
                .toList();
    }


    // Entity -> DTO
    private StoreCreateResponseDto convertToStoreCreateResponseDto(Store store, Category category, Region region, User user) {
        return StoreCreateResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .categoryName(category.getCategoryName())
                .regionName(region.getRegionName())
                .userName(user.getUsername())
                .build();
    }

    private StoreGetResponseDto convertToStoreGetResponseDto(Store store) {
        return StoreGetResponseDto.builder()
                .storeId(store.getStoreId())
                .userId(store.getUser().getUser_id())
                .userName(store.getUser().getUsername())
                .storeName(store.getStoreName())
                .isActive(store.isActive())
                .categoryId(store.getCategory().getCategoryId())
                .categoryName(store.getCategory().getCategoryName())
                .regionId(store.getRegion().getRegionId())
                .regionName(store.getRegion().getRegionName())
                .build();
    }


}
