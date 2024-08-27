package com.sparta.ordersystem.order.management.Menu.dto;

import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuRequestDto {

    UUID store_id;
    String menu_name;
    String content;
    Integer cost;

    public static Menu toEntity(CreateMenuRequestDto requestDto){
        Menu menu = Menu.builder()
                .menu_name(requestDto.getMenu_name())
                .storeId(requestDto.getStore_id())
                .cost(requestDto.getCost())
                .content(requestDto.getContent())
                .isActive(true)
                .build();
        return menu;
    }
}
