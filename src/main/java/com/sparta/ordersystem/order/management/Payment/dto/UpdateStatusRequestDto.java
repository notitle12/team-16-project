package com.sparta.ordersystem.order.management.Payment.dto;

import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UpdateStatusRequestDto {
    private PaymentStatus paymentStatus;
}
