package com.sparta.ordersystem.order.management.Order.repository;

import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderSearchDto;
import com.sparta.ordersystem.order.management.User.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepsoitoryCustom {
    Page<OrderResponseDto> searchOrders(OrderSearchDto searchDto,Pageable pageable, User user);
    OrderResponseDto getOrderById(UUID orderId,Long user_id);
}
