package com.sparta.ordersystem.order.management.Order.dto;

import com.sparta.ordersystem.order.management.Order.entity.OrderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
public class OrderResponseDto extends BaseTimeStamped{
    UUID order_id;

    Long user_id;

    OrderType state;  //주문 상태

    List<OrderMenuDto> order_menu = new ArrayList<>();

}
