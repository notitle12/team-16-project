package com.sparta.ordersystem.order.management.Payment.dto;

import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.Getter;

@Getter
public class UpdateStatusRequestDto {
    private PaymentStatus status;
}
