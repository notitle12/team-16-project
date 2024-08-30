package com.sparta.ordersystem.order.management.Category.service;

import com.sparta.ordersystem.order.management.Category.dto.*;
import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Category.repository.CategoryRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;
    private final String CATEGORY = "카테고리";
    private final String CATEGORY_NAME = "명";
    private final String CATEGORY_ID = "ID";

    @Transactional(readOnly = false)
    public CategoryCreateResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto, User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        String categoryName = categoryCreateRequestDto.getCategoryName();
        String action = "생성";

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicate(CATEGORY_NAME,categoryName);

        Category category = categoryRepository.save(categoryCreateRequestDto.toEntity());
        return convertToCategoryCreateResponseDto(category);
    }


    @Transactional(readOnly = true)
    public CategoryGetResponseDto getCategory(UUID categoryId) {
        String action = "조회";
        Category category  = findCategoryById(categoryId,action);
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
        String categoryName = categoryUpdateRequestDto.getCategoryName();
        String action = "수정";

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicate(CATEGORY_NAME,categoryName);
        Category category  = findCategoryById(categoryId,action);

        category.updateCategoryName(categoryUpdateRequestDto.getCategoryName());

        return convertToCategoryUpdateResponseDto(category);
    }


    @Transactional(readOnly = false)
    public CategoryDeleteResponseDto deleteCategory(UUID categoryId, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        String action = "삭제";
        checkManagerOrMaster(userRoleEnum,action);

        Category category  = findCategoryById(categoryId,action);
        category.softDeleted();

        return convertToCategoryDeleteResponseDto(category);
    }


    /* 카테고리 기준으로 find */
    private Category findCategoryById(UUID categoryId, String action) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> createIllegalArgumentException(CATEGORY_ID, action, categoryId.toString()));
    }

    /* 체크를 위한 메서드들 */

    // 권한 체크 메서드
    private void checkManagerOrMaster(UserRoleEnum userRoleEnum, String action){
        if(userRoleEnum != UserRoleEnum.MASTER && userRoleEnum != UserRoleEnum.MANAGER){
            throw new AccessDeniedException(messageSource.getMessage(
                    "manager.master.possible.action",
                    new String[]{CATEGORY, action ,userRoleEnum.toString()},
                    "권한 체크 부탁드립니다 : "+userRoleEnum.toString(),
                    Locale.getDefault()
            ));
        }
    }

    // 중복 체크 메서드
    private void checkDuplicate(String column, String categoryName){
        if (categoryRepository.existsByNameAndIsActive(categoryName)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.duplicate.item",
                    new String[]{CATEGORY,column,categoryName},
                    "중복 체크 부탁드립니다 : " + categoryName ,
                    Locale.getDefault()
            ));
        }
    }
    // 잘못된 카테고리 ID 체크 메서드
    private IllegalArgumentException createIllegalArgumentException(String column, String action, String invalidValue){
        return new IllegalArgumentException(messageSource.getMessage(
                "illegal.action.invalid",
                new String[]{CATEGORY,column, action, invalidValue},
                "유효하지 않은 값입니다. 다시 확인해 주세요. : " + invalidValue ,
                Locale.getDefault()
        ));
    }


    // Entity -> Dto 변환 메서드
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
