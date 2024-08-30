package com.sparta.ordersystem.order.management.Category.service;

import com.sparta.ordersystem.order.management.Category.dto.*;
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

        if(userRoleEnum != UserRoleEnum.MASTER && userRoleEnum != UserRoleEnum.MANAGER){
            throw new AccessDeniedException("관리자만 카테고리 생성 가능합니다.");
        }


        if (categoryRepository.existsByCategoryName(categoryCreateRequestDto.getCategoryName())) {
            throw new IllegalArgumentException("카테고리 이름이 중복입니다.");
        }


        Category category = categoryRepository.save(categoryCreateRequestDto.toEntity());
        return convertToCategoryCreateResponseDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryGetResponseDto getCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Illegal CategoryId")
        );

        return convertToCategoryGetResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryGetResponseDto> getAllCategory(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page,size, sort);


        return categoryRepository.findAllByIsActiveTrue(pageable).stream()
                .map(this::convertToCategoryGetResponseDto)
                .toList();
    }

    @Transactional(readOnly = false)
    public CategoryUpdateResponseDto updateCategory(UUID categoryId,
                                                    CategoryUpdateRequestDto categoryUpdateRequestDto,
                                                    User user) {

        UserRoleEnum userRoleEnum = user.getRole();

        if(userRoleEnum != UserRoleEnum.MASTER && userRoleEnum != UserRoleEnum.MANAGER){
            throw new AccessDeniedException("관리자만 카테고리 수정 가능합니다.");
        }


        if(categoryRepository.existsByCategoryName(categoryUpdateRequestDto.getCategoryName())){
            throw new IllegalArgumentException("카테고리 이름이 중복입니다.");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->  new NullPointerException("해당 카테고리를 찾을 수 없습니다"));


        category.updateCategoryName(categoryUpdateRequestDto.getCategoryName());

        return convertToCategoryUpdateResponseDto(category);
    }


    @Transactional(readOnly = false)
    public CategoryDeleteResponseDto deleteCategory(UUID categoryId, User user) {

        UserRoleEnum userRoleEnum = user.getRole();

        if(userRoleEnum != UserRoleEnum.MASTER && userRoleEnum != UserRoleEnum.MANAGER){
            throw new AccessDeniedException("관리자만 카테고리 삭제 가능합니다.");
        }


        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->  new NullPointerException("해당 카테고리를 찾을 수 없습니다"));

        category.softDeleted();

        return convertToCategoryDeleteResponseDto(category);
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

    private CategoryUpdateResponseDto convertToCategoryUpdateResponseDto(Category category) {
        return CategoryUpdateResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    private CategoryDeleteResponseDto convertToCategoryDeleteResponseDto(Category category) {
        return CategoryDeleteResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .isActive(category.isActive())
                .build();
    }
}
