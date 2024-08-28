package com.sparta.ordersystem.order.management.Payment.service;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.repository.PaymentRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
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

    @Transactional(readOnly = true)
    public void cretePayment(CreatePaymentRequestDto requestDto) {

        Order order = orderRepository.findByOrderIdAndIsActiveTrue(requestDto.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException(
                        messageSource.getMessage("not.found.order.id",new UUID[]{requestDto.getOrderId()},
                                "존재하지 않는 주문 ID",
                        Locale.getDefault())));


        Payment payment = convertDtoToEntity(requestDto, order);

        paymentRepository.save(payment);

    }


    private Payment convertDtoToEntity(CreatePaymentRequestDto requestDto, Order order){
        return Payment.builder()
                .method(requestDto.getPaymentMethod())
                .status(requestDto.getPaymentStatus())
                .total_price(requestDto.getPrice())
                .order(order)
                .build();
    }
}
