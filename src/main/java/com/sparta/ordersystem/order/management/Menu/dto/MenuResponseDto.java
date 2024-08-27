package com.sparta.ordersystem.order.management.Menu.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public class MenuResponseDto {
    UUID menu_id;
    UUID store_id;
    String menu_name;
}
