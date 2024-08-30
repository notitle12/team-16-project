package com.sparta.ordersystem.order.management.Order.dto;

import com.sparta.ordersystem.order.management.Order.entity.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderSearchDto {
    OrderStatus orderStatus;
    Boolean isActive;

}
