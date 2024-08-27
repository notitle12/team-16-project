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

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MessageSource messageSource;

    public Menu createMenu(CreateMenuRequestDto requestDto) {
        //TODO : 가게 ID 존재 검증 추가
        UUID storeId = requestDto.getStore_id();

        Menu menu = Menu.builder()
                .menu_name(requestDto.getMenu_name())
                .store_id(requestDto.getStore_id())
                .cost(requestDto.getCost())
                .content(requestDto.getContent())
                .isActive(true)
                .build();

        return menuRepository.save(menu);
    }

    public void deleteMenu(UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndAndIsActiveTrue(menuId).orElseThrow(
                () -> new IllegalArgumentException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault()))
        );

        menu.deleteMenu();

        menuRepository.save(menu);
    }

    @Transactional
    public MenuResponseDto updateMenu(UpdateRequestDto updateRequestDto,UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndAndIsActiveTrue(menuId).orElseThrow(
                () -> new IllegalArgumentException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault())
            )
        );

        //해당 가게에 있는 메뉴인지 판단
        menu.updateMenu(updateRequestDto);

        Menu newMenu = menuRepository.save(menu);
        return MenuResponseDto.builder()
                .menu_id(newMenu.getMenuId())
                .menu_name(newMenu.getMenu_name())
                .store_id(newMenu.getStore_id())
                .build();
    }
}
