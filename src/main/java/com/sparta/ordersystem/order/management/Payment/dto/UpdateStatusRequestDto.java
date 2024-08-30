package com.sparta.ordersystem.order.management.Payment.dto;

import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStatusRequestDto {
    private PaymentStatus paymentStatus;
}
