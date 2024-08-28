package com.sparta.ordersystem.order.management.Payment.controller;

import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.service.PaymentService;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequestDto requestDto) {
        paymentService.cretePayment(requestDto);
        return ResponseEntity.ok().body("결제가 저장되었습니다.");
    }
}
