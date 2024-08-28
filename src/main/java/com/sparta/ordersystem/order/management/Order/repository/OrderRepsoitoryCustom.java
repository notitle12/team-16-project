package com.sparta.ordersystem.order.management.Order.repository;

import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepsoitoryCustom {
    Page<OrderResponseDto> searchOrders(Pageable pageable,Long user_id);
    OrderResponseDto getOrderById(UUID orderId,Long user_id);
}
