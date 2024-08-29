package com.sparta.ordersystem.order.management.Delivery.controller;

import com.sparta.ordersystem.order.management.Delivery.dto.CreateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.dto.UpdateDeliveryRequestDto;
import com.sparta.ordersystem.order.management.Delivery.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

}
