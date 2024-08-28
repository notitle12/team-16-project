package com.sparta.ordersystem.order.management.Payment.controller;

import com.sparta.ordersystem.order.management.Payment.dto.createPaymentRequestDto;
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
    public ResponseEntity<?> createPayment(@RequestBody createPaymentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paymentService.cretePayment(requestDto,userDetails.getUser());
        return ResponseEntity.ok().body("결제가 저장되었습니다.");
    }
}
