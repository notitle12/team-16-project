package com.sparta.ordersystem.order.management.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderMenuDto {
    UUID menu_id;
    String menu_name;
    Integer cost;
    String content;

}
