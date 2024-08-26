package com.sparta.ordersystem.order.management.Menu.dto;

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
}
