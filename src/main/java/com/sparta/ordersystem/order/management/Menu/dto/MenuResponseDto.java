package com.sparta.ordersystem.order.management.Menu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class MenuResponseDto {
    UUID menu_id;
    UUID store_id;
    String menu_name;
}
