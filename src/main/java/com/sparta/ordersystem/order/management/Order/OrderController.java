package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Order.dto.createOrderRequestDto;
import com.sparta.ordersystem.order.management.Order.dto.updateOrderStateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody createOrderRequestDto requestDto) {
        try {
            orderService.createOrder(requestDto);
            return ResponseEntity.ok().body("Order created successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("{order_id}")
    public ResponseEntity<?> updateOrder(@RequestBody updateOrderStateRequestDto requestDto, @PathVariable UUID order_id)
    {
        try {
            Order newOrder = orderService.updateOrderState(requestDto.getOrderType(),order_id);
            return ResponseEntity.ok().body("Order updated successfully with id " + newOrder.getOrder_id()
            + " state " +newOrder.getState());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /***
     * 기본 서치기능은 10으로 고정
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok().body(orderService.getAllOrders(pageable));
    }

}
