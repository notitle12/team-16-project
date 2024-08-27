package com.sparta.ordersystem.order.management.Menu.service;

import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.dto.MenuResponseDto;
import com.sparta.ordersystem.order.management.Menu.dto.UpdateRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MessageSource messageSource;

    /***
     * 메뉴 등록
     * TODO 08.27 : 가게 ID 존재 검증 추가
     * @param requestDto
     * @return
     */
    @Transactional
    public Menu createMenu(CreateMenuRequestDto requestDto) {

        UUID storeId = requestDto.getStore_id();

        Menu menu = CreateMenuRequestDto.toEntity(requestDto);

        return menuRepository.save(menu);
    }

    /***
     * 등록된 메뉴를 숨김으로 상태 변경
     * @param menuId
     */
    @Transactional
    public void deleteMenu(UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndIsActiveTrue(menuId).orElseThrow(
                () -> new IllegalArgumentException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault()))
        );

        menu.deleteMenu();

        menuRepository.save(menu);
    }

    /***
     * 메뉴의 정보를 수정(바꾸려고하는 전체의 정보를 담고있다)
     * TODO : 1. 가게가 존재하는지 판단 후 가게에 메뉴가 있는 지 판단
     *
     * @param updateRequestDto
     * @param menuId
     * @return
     */
    @Transactional
    public MenuResponseDto updateMenu(UpdateRequestDto updateRequestDto,UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndIsActiveTrue(menuId).orElseThrow(
                () -> new IllegalArgumentException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault())
            )
        );

        menu.updateMenu(updateRequestDto);

        Menu newMenu = menuRepository.save(menu);

        return convertMenuToResponseDto(newMenu);
    }

    /***
     * 가게에 있는 모든 메뉴들을 조회
     * TODO: 가게가 존재하는지 검증 추가
     * @param storeId
     * @return
     */
    public List<MenuResponseDto> getAllMenus(UUID storeId) {

        List<Menu> menuList = menuRepository.findByStoreIdAndIsActiveTrue(storeId);

        return menuList.stream().map(this::convertMenuToResponseDto).toList();

    }

    /***
     * Entity to ResponseDto 변환함수
     * @param menu
     * @return
     */
    private MenuResponseDto convertMenuToResponseDto(Menu menu) {
        return MenuResponseDto.builder()
                .menu_id(menu.getMenuId())
                .menu_name(menu.getMenu_name())
                .store_id(menu.getStoreId())
                .created_at(menu.getCreated_at())
                .updated_at(menu.getUpdated_at())
                .content(menu.getContent())
                .cost(menu.getCost())
                .is_active(menu.isActive())
                .build();
    }
}
