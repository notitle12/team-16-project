package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Menu.Menu;
import com.sparta.ordersystem.order.management.Menu.MenuRepository;
import com.sparta.ordersystem.order.management.Order.dto.OrderMenuDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.dto.CreateOrderRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
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
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
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
                .orderId(orderId)
                .state(OrderType.create)
                .build();

        given(orderRepository.findById(order.getOrderId())).willReturn(Optional.of(order));

        given(orderRepository.save(order)).willReturn(order);

        Order newOrder = orderService.updateOrderState(OrderType.running, orderId);

        assertEquals(order.getOrderId(), newOrder.getOrderId());
        assertEquals(newOrder.getState(), OrderType.running);
    }

    @Test
    @DisplayName("search조회기능 테스트코드")
    void testSearchOrders(){
        // Given
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지당 10개 항목
        OrderResponseDto orderDto = OrderResponseDto.builder()
                .order_id(UUID.randomUUID())
                .user_id(1L)
                .state(OrderType.create)
                .order_menu(List.of(new OrderMenuDto(UUID.randomUUID(), "Menu1")))
                        .build();

        Page<OrderResponseDto> expectedPage = new PageImpl<>(List.of(orderDto), pageable, 1);

        given(orderRepository.searchOrders(pageable)).willReturn(expectedPage);

        // When
        Page<OrderResponseDto> result = orderService.getAllOrders(pageable);

        // Then
        assertEquals(expectedPage.getContent().size(), result.getContent().size());
        assertEquals(expectedPage.getContent().get(0).getOrder_id(), result.getContent().get(0).getOrder_id());
    }

    @Test
    @DisplayName("주문 삭제 시 존재하지 않은 주문 ID로 실패케이스")
    void testErrorDeleteOrderIdNotExistOrderId(){
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        Exception thrown = assertThrows(IllegalArgumentException.class,
            () -> orderService.deleteOrder(orderId)
        );

        assertEquals("존재하지 않는 주문 ID입니다.",thrown.getMessage());
    }

    @Test
    @DisplayName("주문 삭제 성공케이스")
    void testSuccessDeleteOrder(){
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .orderId(orderId)
                .state(OrderType.create)
                .isActive(false)
                .build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderRepository.save(order)).willReturn(order);

        Order deleteOrder = orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).save(order);
        assertEquals(order.getOrderId(), deleteOrder.getOrderId());
        assertEquals(order.getState(), OrderType.create);
        assertEquals(order.getIsActive(), deleteOrder.getIsActive());
    }
}