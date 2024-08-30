package com.sparta.ordersystem.order.management.Payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ordersystem.order.management.Order.entity.QOrder;
import com.sparta.ordersystem.order.management.Payment.dto.PaymentResponseDto;
import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.entity.QPayment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    /***
     * 최근 결제내역 정렬하여 조회
     * @param userId
     * @return
     */
    @Override
    public List<PaymentResponseDto> getAllPaymentsByUserId(Long userId) {
        QPayment qPayment = QPayment.payment;
        QOrder qOrder = QOrder.order;

        List<Payment> paymentList = jpaQueryFactory.selectFrom(qPayment)
                .leftJoin(qPayment.order,qOrder).fetchJoin()
                .where(qOrder.user.user_id.eq(userId))
                .orderBy(qPayment.created_at.desc())
                .fetch();


        return paymentList.stream().map(this::convertPaymentToPaymentResponseDto).toList();
    }

    private PaymentResponseDto convertPaymentToPaymentResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .orderId(payment.getOrder().getOrderId())
                .paymentMethod(payment.getMethod())
                .paymentStatus(payment.getStatus())
                .totalPrice(payment.getTotal_price())
                .paymentId(payment.getPaymentId())
                .createdAt(payment.getCreated_at())
                .updatedAt(payment.getUpdated_at())
                .build();
    }
}
