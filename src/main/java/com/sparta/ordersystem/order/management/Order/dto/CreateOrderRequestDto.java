package com.sparta.ordersystem.order.management.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequestDto {
    List<UUID> menu_ids;
}
