package com.sparta.ordersystem.order.management.Payment.service;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.dto.PaymentResponseDto;
import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MessageSource messageSource;

    @Transactional
    public void cretePayment(CreatePaymentRequestDto requestDto) {

        Order order = orderRepository.findByOrderIdAndIsActiveTrue(requestDto.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException(
                        messageSource.getMessage("not.found.order.id",new UUID[]{requestDto.getOrderId()},
                                "존재하지 않는 주문 ID",
                        Locale.getDefault())));

        Payment payment = requestDto.toEntity(order);

        paymentRepository.save(payment);

    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentsInDetail(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException(
                        messageSource.getMessage("not.found.payment.id",new UUID[]{paymentId},
                                "존재하지 않는 결제 ID입니다.",
                                Locale.getDefault()))
        );

        return convertPaymentToResponseDto(payment);
    }

    private PaymentResponseDto convertPaymentToResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .paymentStatus(payment.getStatus())
                .paymentMethod(payment.getMethod())
                .createdAt(payment.getCreated_at())
                .updatedAt(payment.getUpdated_at())
                .deletedAt(payment.getDeleted_at())
                .orderId(payment.getOrder().getOrderId())
                .totalPrice(payment.getTotal_price())
                .build();
    }
}
