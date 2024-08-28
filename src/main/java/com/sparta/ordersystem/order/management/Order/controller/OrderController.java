package com.sparta.ordersystem.order.management.Order.controller;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.service.OrderService;
import com.sparta.ordersystem.order.management.Order.dto.CreateOrderRequestDto;
import com.sparta.ordersystem.order.management.Order.dto.UpdateOrderStateRequestDto;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /***
     * 주문 건을 등록
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            orderService.createOrder(requestDto,userDetails.getUser());
            return ResponseEntity.ok().body("Order created successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * 주문 상태를 수정하는 함수
     * @param requestDto
     * @param order_id
     * @return
     */
    @PatchMapping("{order_id}")
    public ResponseEntity<?> updateOrderState(@RequestBody UpdateOrderStateRequestDto requestDto, @PathVariable UUID order_id)
    {
        try {
            Order newOrder = orderService.updateOrderState(requestDto.getOrderType(),order_id);
            return ResponseEntity.ok().body("Order updated successfully with id " + newOrder.getOrderId()
            + " state " +newOrder.getState());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * 사용자의 전체 주문을 조회
     * TODO : 추후 사용자 ID를 추가해서 조건문 추가예정
     * 기본 서치기능은 10으로 고정
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders(@PageableDefault(size = 10) Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok().body(orderService.getAllOrders(pageable,userDetails.getUser()));
    }

    /***
     * 주문 삭제
     * 실제 데이터를 삭제하지 않고 숨김처리 softDelete
     * @param order_id
     * @return
     */
    @DeleteMapping("{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID order_id) {
        try{
            orderService.deleteOrder(order_id);
            return ResponseEntity.ok().body(order_id + "의 주문이 삭제되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /***
     * 주문 ID를 이용하여 주문 상세 조회
     * @param order_id
     * @return
     */
    @GetMapping("{order_id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID order_id,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(orderService.getOrderById(order_id,userDetails.getUser()));

    }
}
