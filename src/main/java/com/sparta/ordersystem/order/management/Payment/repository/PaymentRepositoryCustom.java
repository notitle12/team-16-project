package com.sparta.ordersystem.order.management.Payment.repository;

import com.sparta.ordersystem.order.management.Payment.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentRepositoryCustom {
    List<PaymentResponseDto> getAllPaymentsByUserId(Long userId);
}
