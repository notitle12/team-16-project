package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Menu.Menu;
import com.sparta.ordersystem.order.management.Menu.MenuRepository;
import com.sparta.ordersystem.order.management.Order.dto.createOrderRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 등록 시 존재하지 않는 메뉴로 인한 에러")
    void testErrorCreateOrderNotExistMenuId(){
        // given
        UUID menuId1 = UUID.randomUUID();
        UUID menuId2 = UUID.randomUUID();
        createOrderRequestDto requestDto = createOrderRequestDto.builder()
                .user_id(1L)
                .menu_ids(Arrays.asList(menuId1, menuId2))
                .build();

        given(menuRepository.findById(menuId1)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(requestDto)
        );

        // then
        assertEquals(exception.getMessage(),"Menu" + menuId1 + "does not exist");
    }

    @Test
    @DisplayName("주문 등록 성공 케이스")
    void testSuccessCreateOrder(){
        // given
        UUID menuId1 = UUID.randomUUID();
        createOrderRequestDto requestDto = createOrderRequestDto.builder()
                .user_id(1L)
                .menu_ids(Arrays.asList(menuId1))
                .build();

        Menu menu = Menu.builder()
                .menu_id(menuId1)
                .build();

        given(menuRepository.findById(menuId1)).willReturn(Optional.of(menu));

        orderService.createOrder(requestDto);
        // then
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 상태 변경 API - 존재하지 않는 주문ID 실패케이스")
    void testErrorUpdateOrderStateNotExistOrderId()
    {
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.updateOrderState(OrderType.running, orderId)
        );

        assertEquals("존재하지 않는 주문입니다.",exception.getMessage());
    }

    @Test
    @DisplayName("주문 상태 변경 API - 성공케이스")
    void testSuccessUpdateOrderState()
    {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .order_id(orderId)
                .state(OrderType.create)
                .build();

        given(orderRepository.findById(order.getOrder_id())).willReturn(Optional.of(order));

        given(orderRepository.save(order)).willReturn(order);

        Order newOrder = orderService.updateOrderState(OrderType.running, orderId);

        assertEquals(order.getOrder_id(), newOrder.getOrder_id());
        assertEquals(newOrder.getState(), OrderType.running);
    }
}