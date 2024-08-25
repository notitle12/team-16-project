package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepsoitoryCustom {
    Page<OrderResponseDto> searchOrders(Pageable pageable);
}
