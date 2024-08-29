package com.sparta.ordersystem.order.management.Delivery.dto;

import com.sparta.ordersystem.order.management.Delivery.entity.Delivery;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateDeliveryRequestDto {

    UUID order_id;
    String address;
    String request_note;

    public Delivery toEntity(boolean isActive, Order order){
        return Delivery.builder()
                .address(address)
                .request_note(request_note)
                .isActive(isActive)
                .order(order)
                .build();
    }
}
