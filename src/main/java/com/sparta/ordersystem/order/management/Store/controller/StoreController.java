package com.sparta.ordersystem.order.management.Store.controller;

import com.sparta.ordersystem.order.management.Store.dto.*;
import com.sparta.ordersystem.order.management.Store.service.StoreService;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public StoreCreateResponseDto createStore(@RequestBody @Valid StoreCreateRequestDto storeCreateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return storeService.createStore(storeCreateRequestDto, userDetails.getUser());
    }

    @GetMapping("/store/{store_id}")
    public StoreGetResponseDto getStore(@PathVariable UUID store_id){
        return storeService.getStore(store_id);
    }

    @GetMapping("/store")
    public List<StoreGetResponseDto> getAllStore(
            @RequestParam(name="region_id",required = false) UUID regionId,
            @RequestParam(name="category_id", required = false) UUID categoryId,
            @RequestParam(name="page") int page,
            @RequestParam(name="size") int size,
            @RequestParam(name="sortBy") String sortBy,
            @RequestParam(name="isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
) {

        UserRoleEnum userRoleEnum = userDetails.getUser().getRole();
        if(isManagerAndMaster(userRoleEnum)){
            return storeService.getAllStore(page-1, size, sortBy, isAsc);
        }

        if(isOwner(userRoleEnum)){
            return storeService.getAllStoreByUser(userDetails.getUser(), page-1, size,sortBy, isAsc);
        }

        if (isMember(userRoleEnum)){
            if (regionId != null && categoryId == null) {
                return storeService.getAllStoreByRegion(regionId, page-1, size, sortBy, isAsc);
            } else if (regionId != null && categoryId != null) {
                return storeService.getAllStoreByRegionAndCategory(regionId, categoryId, page-1, size, sortBy, isAsc);
            }
        }

        throw new AccessDeniedException("접근 권한이 없습니다.");
    }

    private boolean isMember(UserRoleEnum userRoleEnum) {
        return (userRoleEnum != UserRoleEnum.OWNER && userRoleEnum != UserRoleEnum.MANAGER && userRoleEnum != UserRoleEnum.MANAGER);
    }

    private boolean isOwner(UserRoleEnum userRoleEnum){
        return userRoleEnum == UserRoleEnum.OWNER;
    }

    private boolean isManagerAndMaster(UserRoleEnum userRoleEnum){
        return (userRoleEnum == UserRoleEnum.MANAGER || userRoleEnum == UserRoleEnum.MASTER);
    }


    @PatchMapping("/store/{store_id}")
    public StoreUpdateResponseDto updateStore(@PathVariable(name="store_id") UUID storeId,
                                              @RequestBody StoreUpdateRequestDto storeUpdateRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return storeService.updateService(storeId, storeUpdateRequestDto, userDetails.getUser() );
    }
}
