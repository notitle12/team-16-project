package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Menu.Menu;
import com.sparta.ordersystem.order.management.Menu.MenuRepository;
import com.sparta.ordersystem.order.management.Order.dto.createOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MenuRepository menuRepository;
    /**
     * 주문을 등록해주는 메소드
     * @param requestDto
     */
    public void createOrder(createOrderRequestDto requestDto) {

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

}
