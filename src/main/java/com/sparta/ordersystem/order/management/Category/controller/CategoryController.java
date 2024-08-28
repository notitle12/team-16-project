package com.sparta.ordersystem.order.management.Category.controller;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public CategoryCreateResponseDto createCategory(@RequestBody @Valid  CategoryCreateRequestDto categoryCreateRequestDto){
        return categoryService.createCategory(categoryCreateRequestDto);
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
