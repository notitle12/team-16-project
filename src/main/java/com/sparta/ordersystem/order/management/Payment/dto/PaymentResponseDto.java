package com.sparta.ordersystem.order.management.Payment.dto;

import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentMethod;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class PaymentResponseDto {

    private UUID paymentId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private UUID orderId;
    private Integer totalPrice;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
