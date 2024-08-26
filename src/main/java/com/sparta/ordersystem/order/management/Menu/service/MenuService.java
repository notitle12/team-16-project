package com.sparta.ordersystem.order.management.Menu.service;

import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
}
