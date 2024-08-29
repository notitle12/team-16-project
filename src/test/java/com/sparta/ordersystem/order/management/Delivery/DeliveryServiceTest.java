package com.sparta.ordersystem.order.management.Delivery;

import com.sparta.ordersystem.order.management.Delivery.dto.CreateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.entity.Delivery;
import com.sparta.ordersystem.order.management.Delivery.repository.DeliveryRepository;
import com.sparta.ordersystem.order.management.Delivery.service.DeliveryService;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    @DisplayName("배송지 등록 - 성공케이스")
    void testSuccessCreateDelivery(){
        UUID orderId = UUID.randomUUID();

        CreateDeliveryRequestDto requestDto = CreateDeliveryRequestDto.builder()
                .order_id(orderId)
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.of(new Order()));

        deliveryService.createDelivery(requestDto);

        verify(deliveryRepository,times(1)).save(any(Delivery.class));
    }

    @Test
    @DisplayName("배송지 등록 - 존재하지 않는 주문 ID")
    void testErrorCreateDeliveryNotExistedOrderId(){
        UUID orderId = UUID.randomUUID();

        CreateDeliveryRequestDto requestDto = CreateDeliveryRequestDto.builder()
                .order_id(orderId)
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> deliveryService.createDelivery(requestDto));

        assertEquals("존재하지 않는 주문 ID입니다.", exception.getMessage());
    }


}
