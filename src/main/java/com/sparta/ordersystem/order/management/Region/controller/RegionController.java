package com.sparta.ordersystem.order.management.Region.controller;


import com.sparta.ordersystem.order.management.Region.dto.RegionCreateRequestDto;
import com.sparta.ordersystem.order.management.Region.dto.RegionCreateResponseDto;
import com.sparta.ordersystem.order.management.Region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/region")
    public RegionCreateResponseDto createRegion(@RequestBody RegionCreateRequestDto regionCreateRequestDto){
        return regionService.createRegion(regionCreateRequestDto);
    }
}
