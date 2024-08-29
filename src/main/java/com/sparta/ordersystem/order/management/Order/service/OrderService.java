package com.sparta.ordersystem.order.management.Order.service;

import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Order.dto.OrderSearchDto;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.entity.OrderStatus;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.dto.CreateOrderRequestDto;
import com.sparta.ordersystem.order.management.Order.exception.OrderCancelException;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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
    public void createOrder(CreateOrderRequestDto requestDto, User user) {

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.CREATE)
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
     * 주문 생성 후 5분 이내에만 취소 가능
     * @param orderStatus
     * @param orderId
     */
    @Transactional
    public Order updateOrderState(OrderStatus orderStatus, UUID orderId, User user) {
        //존재하는 주문인지 검증
        Order order = orderRepository.findByOrderIdAndIsActiveTrue(orderId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주문입니다."));


        //주문 취소인 경우
        if(OrderStatus.CANCEL.equals(orderStatus))
        {
            //고객인 경우
            if(UserRoleEnum.CUSTOMER.equals(user.getRole()) && !order.getUser().getId().equals(user.getId()))
            {
                throw new AccessDeniedException("본인의 주문만 취소할 수 있습니다.");
            }
            //Duration : 날짜간의 시간차이를 계산
            // 주문 생성 시간과 현재 시간을 비교하여 5분 이내인지 확인합니다.
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(order.getCreated_at(), now);

            if (duration.toMinutes() > 5) {
                throw new OrderCancelException("주문 생성 후 5분이 지나서 취소할 수 없습니다.");
            }
        }

        //주문 취소나 주문 확정으로의 상태로 변경
        order.updateState(orderStatus);

        return orderRepository.save(order);
    }

    /***
     * 주문 전체 조회
     * - 고객:자신의 주문 내역만 조회 가능
     * - 가게 주인: 자신의 가게 주문 내역, 가게 정보, 주문 처리 및 메뉴 수정 가능
     * - 관리자: 모든 가게 및 주문에 대한 전체 권한 보유
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(OrderSearchDto searchDto,Pageable pageable, User user) {

        return orderRepository.searchOrders(searchDto,pageable,user);
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
    public OrderResponseDto getOrderById(UUID orderId,User user) {

        //주문 아이디 존재 검증
        Order order = orderRepository.findByOrderIdAndIsActiveTrue(orderId).orElseThrow(
                () -> new IllegalArgumentException(
                        messageSource.getMessage("not.found.order.id",new UUID[]{orderId},"존재하지 않는 주문 ID",
                                Locale.getDefault())
                )
        );

        return orderRepository.getOrderById(orderId, user.getId());

    }
}
