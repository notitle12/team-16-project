package com.sparta.ordersystem.order.management.Payment.dto;


import com.sparta.ordersystem.order.management.Payment.entity.PaymentMethod;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class createPaymentRequestDto {
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private UUID orderId;
    private Integer price;
}
