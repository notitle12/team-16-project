package com.sparta.ordersystem.order.management.Region.service;

import com.sparta.ordersystem.order.management.Region.dto.*;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Region.repository.RegionRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;

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
public class RegionService {

    private final RegionRepository regionRepository;
    private final MessageSource messageSource;

    private final String REGION = "지역";
    private final String REGION_NAME = "명";
    private final String REGION_ID = "ID";

    private final String CREATE_ACTION = "생성";
    private final String UPDATE_ACTION = "수정";
    private final String GET_ACTION = "조회";
    private final String DELETE_ACTION = "삭제";

    /**
     * 관리자 또는 마스터가 새로운 지역 생성
     *
     * @param regionCreateRequestDto 생성할 지역 정보
     * @param user 지역 생성 요청을 한 사용자
     * @return 생성된 지역 정보
     */
    @Transactional(readOnly = false)
    public RegionCreateResponseDto createRegion(RegionCreateRequestDto regionCreateRequestDto, User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        String regionName = regionCreateRequestDto.getRegionName();
        String action = CREATE_ACTION;

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicateByRegionName(regionName);

        Region region = regionRepository.save(regionCreateRequestDto.toEntity());
        return convertToRegionCreateResponseDto(region);
    }

    /**
     * 지역 단건 조회
     *
     * @param regionId 조회할 지역 Id
     * @return 조회한 지역 정보
     */
    @Transactional(readOnly = true)
    public RegionGetResponseDto getRegion(UUID regionId) {
        String action = GET_ACTION;
        Region region  = findRegionById(regionId);
        return convertToRegionGetResponseDto(region);
    }

    /**
     * 페이지 및 정렬 기준으로 지역 전체 조회
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 당 표시할 아이템 개수
     * @param sortBy 정렬 기준
     * @param isAsc 정렬 방법 (asc , desc)
     * @return 조회한 지역 정보 전체
     */
    @Transactional(readOnly = true)
    public List<RegionGetResponseDto> getAllRegion(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page,size, sort);


        return regionRepository.findAllByIsActiveTrue(pageable).stream()
                .map(this::convertToRegionGetResponseDto)
                .toList();
    }

    /**
     * 관리자 또는 마스터가 지역 정보 수정
     *
     * @param regionId 수정할 지역 ID
     * @param regionUpdateRequestDto 수정할 지역 정보
     * @param user 지역 수정을 요청한 사용자
     * @return 수정된 지역 정보
     */
    @Transactional(readOnly = false)
    public RegionUpdateResponseDto updateRegion(UUID regionId,
                                                    RegionUpdateRequestDto regionUpdateRequestDto,
                                                    User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        String regionName = regionUpdateRequestDto.getRegionName();
        String action = UPDATE_ACTION;

        checkManagerOrMaster(userRoleEnum,action);
        checkDuplicateByRegionName(regionName);
        Region region  = findRegionById(regionId);

        region.updateRegionName(regionUpdateRequestDto.getRegionName());

        return convertToRegionUpdateResponseDto(region);
    }


    /**
     * 관리자 또는 마스터가 지역 삭제
     *
     * @param regionId 삭제할 지역 Id
     * @param user 삭제 요청한 사용자
     * @return 삭제 처리된 지역 정보
     */
    @Transactional(readOnly = false)
    public RegionDeleteResponseDto deleteRegion(UUID regionId, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        String action = DELETE_ACTION;
        checkManagerOrMaster(userRoleEnum,action);

        Region region  = findRegionById(regionId);
        region.softDeleted(user.getUser_id());

        return convertToRegionDeleteResponseDto(region);
    }


    /**
     * 주어진 지역 Id를 기준으로 지역 엔티티 조회
     *
     * @param regionId 조회하려는 지역 Id
     * @return 조회한 지역 엔티티
     */
    private Region findRegionById(UUID regionId) {
        return regionRepository.findByRegionIdAndIsActiveTrue(regionId)
                .orElseThrow(() -> createNullPointerException(REGION_ID, regionId.toString()));
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
                new String[]{REGION, column, value},
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
                    new String[]{REGION, action ,userRoleEnum.toString()},
                    "권한 체크 부탁드립니다 : "+userRoleEnum.toString(),
                    Locale.getDefault()
            ));
        }
    }

    /**
     * 지역 명에 대한 중복 여부 체크
     *
     * @param regionName 중복 체크할 지역 명
     */
    private void checkDuplicateByRegionName(String regionName){
        if (regionRepository.existsByRegionNameAndIsActiveTrue(regionName)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "error.duplicate.item",
                    new String[]{REGION,REGION_NAME,regionName},
                    "중복 체크 부탁드립니다 : " + regionName ,
                    Locale.getDefault()
            ));
        }
    }



    /**
     * Region -> RegionCreateResponseDto 변환
     *
     * @param region 변환할 지역 엔티티
     * @return 생성된 지역 정보 담은 Dto
     */
    private RegionCreateResponseDto convertToRegionCreateResponseDto(Region region) {
        return RegionCreateResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .createdAt(region.getCreated_at())
                .createdBy(region.getCreated_by())
                .build();
    }


    /**
     * Region -> RegionGetResponseDto 변환
     *
     * @param region 변환할 지역 엔티티
     * @return 조회한 지역 정보 담은 Dto
     */
    private RegionGetResponseDto convertToRegionGetResponseDto(Region region) {
        return RegionGetResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }

    /**
     * Region -> RegionUpdateResponseDto 변환
     *
     * @param region 변환할 지역 엔티티
     * @return 수정한 지역 정보 담은 Dto
     */
    private RegionUpdateResponseDto convertToRegionUpdateResponseDto(Region region) {
        return RegionUpdateResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .updatedAt(region.getUpdated_at())
                .updatedBy(region.getUpdated_by())
                .build();
    }


    /**
     * Region -> RegionDeleteResponseDto 변환
     *
     * @param region 변환할 지역 엔티티
     * @return 삭제된 지역 정보 담은 Dto
     */
    private RegionDeleteResponseDto convertToRegionDeleteResponseDto(Region region) {
        return RegionDeleteResponseDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .isActive(region.isActive())
                .deletedAt(region.getDeleted_at())
                .deletedBy(region.getDeleted_by())
                .build();
    }
}
