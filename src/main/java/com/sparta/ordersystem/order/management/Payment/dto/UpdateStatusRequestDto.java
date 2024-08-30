package com.sparta.ordersystem.order.management.Payment.dto;

import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class UpdateStatusRequestDto {
    private PaymentStatus paymentStatus;
}
