package com.sparta.ordersystem.order.management.Payment.dto;


import com.sparta.ordersystem.order.management.Payment.entity.PaymentMethod;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreatePaymentRequestDto {
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private UUID orderId;
    private Integer price;
}
