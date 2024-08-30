package com.sparta.ordersystem.order.management.Order.dto;

import com.sparta.ordersystem.order.management.Order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderSearchDto {
    OrderStatus orderStatus;
    boolean isActive;

}
