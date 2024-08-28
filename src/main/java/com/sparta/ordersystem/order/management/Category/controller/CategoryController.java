package com.sparta.ordersystem.order.management.Category.controller;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public CategoryCreateResponseDto createCategory(@RequestBody CategoryCreateRequestDto categoryCreateRequestDto){
        return categoryService.createCategory(categoryCreateRequestDto);
    }
}
