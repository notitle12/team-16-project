package com.sparta.ordersystem.order.management.Region.controller;

import com.sparta.ordersystem.order.management.Region.dto.*;
import com.sparta.ordersystem.order.management.Region.service.RegionService;

import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/region")
    public ResponseEntity<RegionCreateResponseDto> createRegion(@RequestBody @Valid  RegionCreateRequestDto regionCreateRequestDto,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(
                regionService.createRegion(regionCreateRequestDto, userDetails.getUser())
        );
    }


    @GetMapping("/region/{region_id}")
    public ResponseEntity<RegionGetResponseDto> getRegion(@PathVariable UUID region_id){
        return ResponseEntity.ok(
                regionService.getRegion(region_id)
        );
    }

    @GetMapping("/region")
    public ResponseEntity<List<RegionGetResponseDto>> getAllRegions(@RequestParam("page") int page,
                                                                         @RequestParam("size") int size,
                                                                         @RequestParam("sortBy") String sortBy,
                                                                         @RequestParam("isAsc") boolean isAsc){
        return ResponseEntity.ok(
                regionService.getAllRegion(page-1, size, sortBy, isAsc)
        );
    }


    @PatchMapping("/region/{region_id}")
    public ResponseEntity<RegionUpdateResponseDto> updateRegion(@PathVariable UUID region_id,
                                                                    @RequestBody @Valid RegionUpdateRequestDto regionUpdateRequestDto,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(
                regionService.updateRegion(region_id,regionUpdateRequestDto, userDetails.getUser())
        );
    }


    @DeleteMapping("/region/{region_id}")
    public ResponseEntity<RegionDeleteResponseDto> deleteRegion(@PathVariable UUID region_id,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(
                regionService.deleteRegion(region_id, userDetails.getUser())
        );
    }


}
