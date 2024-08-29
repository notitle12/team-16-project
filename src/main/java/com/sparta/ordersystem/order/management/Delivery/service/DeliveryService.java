package com.sparta.ordersystem.order.management.Delivery.service;

import com.sparta.ordersystem.order.management.Delivery.dto.CreateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.entity.Delivery;
import com.sparta.ordersystem.order.management.Delivery.repository.DeliveryRepository;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {


    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createDelivery(CreateDeliveryRequestDto requestDto) {

        //존재하는 주문ID인지 검증
        Order order = orderRepository.findByOrderIdAndIsActiveTrue(requestDto.getOrder_id()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 주문 ID입니다.")
        );

        Delivery delivery = requestDto.toEntity(true,order);

        deliveryRepository.save(delivery);
    }
}
