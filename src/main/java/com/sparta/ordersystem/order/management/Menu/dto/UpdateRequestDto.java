package com.sparta.ordersystem.order.management.Menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateRequestDto {

    UUID store_id;
    String menu_name;
    Integer cost;
    String content;
}
