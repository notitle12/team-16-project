package com.sparta.ordersystem.order.management.Category.service;

import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateRequestDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryCreateResponseDto;
import com.sparta.ordersystem.order.management.Category.dto.CategoryGetResponseDto;
import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Category.repository.CategoryRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = false)
    public CategoryCreateResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto, User user) {

        UserRoleEnum userRoleEnum = user.getRole();

        if(userRoleEnum == UserRoleEnum.CUSTOMER || userRoleEnum == UserRoleEnum.OWNER){
            throw new AccessDeniedException("관리자만 카테고리 생성 가능합니다.");
        }


        if (categoryRepository.existsByCategoryName(categoryCreateRequestDto.getCategoryName())) {
            throw new IllegalArgumentException("카테고리 이름이 중복입니다.");
        }


        Category category = categoryRepository.save(categoryCreateRequestDto.toEntity());
        return convertToCategoryCreateResponseDto(category);
    }

    public CategoryGetResponseDto getCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Illegal CategoryId")
        );

        return convertToCategoryGetResponseDto(category);
    }

    public List<CategoryGetResponseDto> getAllCategory(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page,size, sort);


        return categoryRepository.findAll(pageable).stream()
                .map(this::convertToCategoryGetResponseDto)
                .toList();
    }


    private CategoryCreateResponseDto convertToCategoryCreateResponseDto(Category category) {
        return CategoryCreateResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    private CategoryGetResponseDto convertToCategoryGetResponseDto(Category category) {
        return CategoryGetResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }



}
