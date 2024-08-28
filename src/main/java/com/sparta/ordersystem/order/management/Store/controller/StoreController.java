package com.sparta.ordersystem.order.management.Store.controller;

import com.sparta.ordersystem.order.management.Store.dto.StoreCreateRequestDto;
import com.sparta.ordersystem.order.management.Store.dto.StoreCreateResponseDto;
import com.sparta.ordersystem.order.management.Store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public StoreCreateResponseDto createStore(@RequestBody StoreCreateRequestDto storeCreateRequestDto){
        return storeService.createStore(storeCreateRequestDto);
    }
}
