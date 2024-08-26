package com.sparta.ordersystem.order.management.Menu.service;

import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public Menu createMenu(CreateMenuRequestDto requestDto) {
        //TODO : 가게 ID 존재 검증 추가
        UUID storeId = requestDto.getStore_id();

        Menu menu = Menu.builder()
                .menu_name(requestDto.getMenu_name())
                .store_id(requestDto.getStore_id())
                .cost(requestDto.getCost())
                .content(requestDto.getContent())
                .is_active(true)
                .build();

        return menuRepository.save(menu);
    }
}
