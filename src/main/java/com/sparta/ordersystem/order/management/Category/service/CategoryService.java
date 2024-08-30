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

    private final String CREATE_ACTION = "생성";
    private final String UPDATE_ACTION = "수정";
    private final String GET_ACTION = "조회";
    private final String DELETE_ACTION = "삭제";

    /**
     * 관리자 또는 마스터가 새로운 카테고리 생성
     *
     * @param categoryCreateRequestDto 생성할 카테고리 정보
     * @param user 카테고리 생성 요청을 한 사용자
     * @return 생성된 카테고리 정보
     */
    @Transactional(readOnly = false)
    public CategoryCreateResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto, User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        String categoryName = categoryCreateRequestDto.getCategoryName();
        String action = CREATE_ACTION;

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicateByCategoryName(categoryName);

        Category category = categoryRepository.save(categoryCreateRequestDto.toEntity());
        return convertToCategoryCreateResponseDto(category);
    }

    /**
     * 카테고리 단건 조회
     *
     * @param categoryId 조회할 카테고리 Id
     * @return 조회한 카테고리 정보
     */
    @Transactional(readOnly = true)
    public CategoryGetResponseDto getCategory(UUID categoryId) {
        String action = GET_ACTION;
        Category category  = findCategoryById(categoryId);
        return convertToCategoryGetResponseDto(category);
    }

    /**
     * 페이지 및 정렬 기준으로 카테고리 전체 조회
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 당 표시할 아이템 개수
     * @param sortBy 정렬 기준
     * @param isAsc 정렬 방법 (asc , desc)
     * @return 조회한 카테고리 정보 전체
     */
    @Transactional(readOnly = true)
    public List<CategoryGetResponseDto> getAllCategory(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page,size, sort);


        return categoryRepository.findAllByIsActiveTrue(pageable).stream()
                .map(this::convertToCategoryGetResponseDto)
                .toList();
    }

    /**
     * 관리자 또는 마스터가 카테고리 정보 수정
     *
     * @param categoryId 수정할 카테고리 ID
     * @param categoryUpdateRequestDto 수정할 카테고리 정보
     * @param user 카테고리 수정을 요청한 사용자
     * @return 수정된 카테고리 정보
     */
    @Transactional(readOnly = false)
    public CategoryUpdateResponseDto updateCategory(UUID categoryId,
                                                    CategoryUpdateRequestDto categoryUpdateRequestDto,
                                                    User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        String categoryName = categoryUpdateRequestDto.getCategoryName();
        String action = UPDATE_ACTION;

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicateByCategoryName(categoryName);
        Category category  = findCategoryById(categoryId);

        category.updateCategoryName(categoryUpdateRequestDto.getCategoryName());

        return convertToCategoryUpdateResponseDto(category);
    }


    /**
     * 관리자 또는 마스터가 카테고리 삭제
     *
     * @param categoryId 삭제할 카테고리 Id
     * @param user 삭제 요청한 사용자
     * @return 삭제 처리된 카테고리 정보
     */
    @Transactional(readOnly = false)
    public CategoryDeleteResponseDto deleteCategory(UUID categoryId, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        String action = DELETE_ACTION;
        checkManagerOrMaster(userRoleEnum,action);

        Category category  = findCategoryById(categoryId);
        category.softDeleted();

        return convertToCategoryDeleteResponseDto(category);
    }


    /**
     * 주어진 카테고리 Id를 기준으로 카테고리 엔티티 조회
     *
     * @param categoryId 조회하려는 카테고리 Id
     * @return 조회한 카테고리 엔티티
     */
    private Category findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> createNullPointerException(CATEGORY_ID, categoryId.toString()));
    }

    /**
     * 조회한 항목이 존재하지 않을 시 NullPointerException 생성
     *
     * @param column 체크 대상 컬럼명 (ex ID, 이름 등 )
     * @param value 체크 대상 값
     * @return 커스텀 메시지가 포함된 NullPointerException 객체
     */
    private NullPointerException createNullPointerException(String column,String value){
        return new NullPointerException(messageSource.getMessage(
                "error.null.item",
                new String[]{CATEGORY, column, value},
                "해당 항목이 존재하지 않습니다. 다시 확인해 주세요. : " + value ,
                Locale.getDefault()
        ));
    }



    /**
     * 관리자 또는 마스터 권한 체크
     *
     * @param userRoleEnum 사용자 권한
     * @param action 수행중인 작업 (ex "생성", "수정", 삭제 등)
     */
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

    /**
     * 카테고리 명에 대한 중복 여부 체크
     *
     * @param categoryName 중복 체크할 카테고리 명
     */
    private void checkDuplicateByCategoryName(String categoryName){
        if (categoryRepository.existsByNameAndIsActive(categoryName)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.duplicate.item",
                    new String[]{CATEGORY,CATEGORY_NAME,categoryName},
                    "중복 체크 부탁드립니다 : " + categoryName ,
                    Locale.getDefault()
            ));
        }
    }



    /**
     * Category -> CategoryCreateResponseDto 변환
     *
     * @param category 변환할 카테고리 엔티티
     * @return 생성된 카테고리 정보 담은 Dto
     */
    private CategoryCreateResponseDto convertToCategoryCreateResponseDto(Category category) {
        return CategoryCreateResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }


    /**
     * Category -> CategoryGetResponseDto 변환
     *
     * @param category 변환할 카테고리 엔티티
     * @return 조회한 카테고리 정보 담은 Dto
     */
    private CategoryGetResponseDto convertToCategoryGetResponseDto(Category category) {
        return CategoryGetResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    /**
     * Category -> CategoryUpdateResponseDto 변환
     *
     * @param category 변환할 카테고리 엔티티
     * @return 수정한 카테고리 정보 담은 Dto
     */
    private CategoryUpdateResponseDto convertToCategoryUpdateResponseDto(Category category) {
        return CategoryUpdateResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }


    /**
     * Category -> CategoryDeleteResponseDto 변환
     *
     * @param category 변환할 카테고리 엔티티
     * @return 삭제된 카테고리 정보 담은 Dto
     */
    private CategoryDeleteResponseDto convertToCategoryDeleteResponseDto(Category category) {
        return CategoryDeleteResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .isActive(category.isActive())
                .build();
    }
}
