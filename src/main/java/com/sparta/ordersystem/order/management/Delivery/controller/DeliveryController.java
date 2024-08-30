package com.sparta.ordersystem.order.management.Delivery.controller;

import com.sparta.ordersystem.order.management.Delivery.dto.CreateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.dto.UpdateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.service.DeliveryService;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createDelivery(@Valid @RequestBody CreateDeliveryRequestDto requestDto){

        deliveryService.createDelivery(requestDto);
        return ResponseEntity.ok().body("배송지 등록이 완료되었습니다.");
    }

    @PutMapping("{delivery_id}")
    public ResponseEntity<?> updateDelivery(@PathVariable UUID delivery_id,
                                            @RequestBody UpdateDeliveryRequestDto updateDeliveryRequestDto)
    {
        return ResponseEntity.ok().body(deliveryService.updateDelivery(delivery_id,updateDeliveryRequestDto));
    }

    /***
     * soft delete로 활성화상태만 변경하는 함수 true -> false
     * @param delivery_id
     * @return
     */
    @DeleteMapping("{delivery_id}")
    public ResponseEntity<?> deleteDelivery(@PathVariable UUID delivery_id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.ok().body(deliveryService.deleteDelivery(delivery_id,userDetails.getUser().getUser_id()));
    }

    /***
     * 주문에 저장된 배송지를 조회
     * @param order_id
     * @return
     */
    @GetMapping("/orders/{order_id}")
    public ResponseEntity<?> getDeliveryByOrderId(@PathVariable UUID order_id){
        return ResponseEntity.ok().body(deliveryService.getDeliveryByOrderId(order_id));
    }
}
