package com.sparta.ordersystem.order.management.Category.controller;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryGetResponseDto;
import com.sparta.ordersystem.order.management.Category.service.CategoryService;

import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public CategoryCreateResponseDto createCategory(@RequestBody @Valid  CategoryCreateRequestDto categoryCreateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.createCategory(categoryCreateRequestDto, userDetails.getUser());
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

    @GetMapping("/category/{category_id}")
    public CategoryGetResponseDto getCategory(@PathVariable UUID category_id){
        return categoryService.getCategory(category_id);
    }

    @GetMapping("/category")
    public List<CategoryGetResponseDto> getAllCategories(@RequestParam("page") int page,
                                                         @RequestParam("size") int size,
                                                         @RequestParam("sortBy") String sortBy,
                                                         @RequestParam("isAsc") boolean isAsc){
        return categoryService.getAllCategory(page-1, size, sortBy, isAsc);
    }
}
