package com.sparta.ordersystem.order.management.Order.service;

import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.entity.OrderType;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.dto.CreateOrderRequestDto;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MenuRepository menuRepository;
    private final MessageSource messageSource;

    /**
     * 주문을 등록해주는 메소드
     * @param requestDto
     */
    @Transactional
    public void createOrder(CreateOrderRequestDto requestDto) {

        Order order = Order.builder()
                .user_id(requestDto.getUser_id())
                .state(OrderType.create)
                .build();

        //requestDto 의 메뉴ID들이 모두 다 있는 지 확인
        for(UUID menuId : requestDto.getMenu_ids())
        {
            Menu menu = menuRepository.findById(menuId).orElseThrow(
                    ()-> new IllegalArgumentException("Menu" + menuId + "does not exist")
            );

            order.addMenu(menu);
        }

        orderRepository.save(order);
    }

    /***
     * 주문 상태를 업데이트하는 함수
     * @param orderType
     * @param orderId
     */
    @Transactional
    public Order updateOrderState(OrderType orderType, UUID orderId) {
        //존재하는 주문인지 검증
        Order order = orderRepository.findByOrderIdAndIsActiveTrue(orderId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주문입니다."));

        //주문 취소나 주문 확정으로의 상태로 변경
        order.updateState(orderType);

        return orderRepository.save(order);
    }

    /***
     * 주문 전체 조회
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.searchOrders(pageable);
    }

    /***
     * 주문 삭제 즉 주문의 활성화 상태 변경
     * @param orderId
     * @return
     */
    @Transactional
    public Order deleteOrder(UUID orderId) {

        Order order = orderRepository.findByOrderIdAndIsActiveTrue(orderId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 주문 ID입니다.")
        );

        order.deleteOrder();

        return orderRepository.save(order);
    }

    /***
     * 주문 상세 조회 시 메뉴의 값들도 같이 조회
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId) {

        //주문 아이디 존재 검증
        Order order = orderRepository.findByOrderIdAndIsActiveTrue(orderId).orElseThrow(
                () -> new IllegalArgumentException(
                        messageSource.getMessage("not.found.order.id",new UUID[]{orderId},"존재하지 않는 주문 ID",
                                Locale.getDefault())
                )
        );

        return orderRepository.getOrderById(orderId);

    }
}
