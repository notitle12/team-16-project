package com.sparta.ordersystem.order.management.Region.controller;


import com.sparta.ordersystem.order.management.Region.dto.*;
import com.sparta.ordersystem.order.management.Region.service.RegionService;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/region")
    public RegionCreateResponseDto createRegion(@RequestBody @Valid  RegionCreateRequestDto regionCreateRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return regionService.createRegion(regionCreateRequestDto,userDetails.getUser());
    }

    @GetMapping("/region/{region_id}")
    public RegionGetResponseDto getRegion(@PathVariable UUID region_id){
        return regionService.getRegion(region_id);
    }

    @GetMapping("/region")
    public List<RegionGetResponseDto> getAllRegions(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ){
        return regionService.getAllRegion(page-1,size,sortBy, isAsc);

    }

    @PatchMapping("/region/{region_id}")
    public RegionUpdateResponseDto updateRegion(@PathVariable UUID region_id,
                                                @RequestBody @Valid RegionUpdateRequestDto regionUpdateRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return regionService.updateRegion(region_id, regionUpdateRequestDto, userDetails.getUser());
    }


    @DeleteMapping("/region/{region_id}")
    public RegionDeleteResponseDto deleteRegion(@PathVariable UUID region_id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return regionService.deleteRegion(region_id, userDetails.getUser());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
