package com.sparta.ordersystem.order.management.Order.dto;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.entity.OrderStatus;
import com.sparta.ordersystem.order.management.Order.entity.OrderType;
import com.sparta.ordersystem.order.management.User.entity.User;
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
    OrderType orderType;//대면 or 온라인 주문
    List<UUID> menu_ids;
    UUID store_id;

    public Order toEntity(User user) {
        return Order.builder()
                .user(user)
                .storeId(store_id)
                .orderType(orderType)
                .orderStatus(OrderStatus.CREATE)
                .build();
    }
}
