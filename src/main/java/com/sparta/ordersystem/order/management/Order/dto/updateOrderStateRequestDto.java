package com.sparta.ordersystem.order.management.Order.dto;

import com.sparta.ordersystem.order.management.Order.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class updateOrderStateRequestDto {

    private OrderType orderType;
}
