package com.sparta.ordersystem.order.management.Payment.service;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.exception.OrderNotFoundException;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.dto.PaymentResponseDto;
import com.sparta.ordersystem.order.management.Payment.dto.UpdateStatusRequestDto;
import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.exception.PaymentNotFoundException;
import com.sparta.ordersystem.order.management.Payment.repository.PaymentRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                () -> new OrderNotFoundException(
                        messageSource.getMessage("not.found.order.id",new UUID[]{requestDto.getOrderId()},
                                "존재하지 않는 주문 ID",
                        Locale.getDefault())));

        Payment payment = requestDto.toEntity(order);

        paymentRepository.save(payment);

    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentsInDetail(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException(
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

    @Transactional
    public void updatePaymentStatus(UUID paymentId, UpdateStatusRequestDto requestDto) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException(
                        messageSource.getMessage("not.found.payment.id",new UUID[]{paymentId},
                                "존재하지 않는 결제 ID입니다.",
                                Locale.getDefault()))
        );

        payment.updateStatus(requestDto.getPaymentStatus());

        paymentRepository.save(payment);
    }

    /***
     * 해당 유저의 결제내역 조회하는 비즈니스 로직
     * 1. 해당 유저이 주문했던 주문 내역들을 가져오기
     * 2. 주문 ID를 통해 결제내역들 조회하기
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPaymentsByUserId(User user) {
        List<PaymentResponseDto> responseDtoList =  paymentRepository.getAllPaymentsByUserId(user.getUser_id());

        if(responseDtoList.isEmpty() || responseDtoList.size() == 0){
            throw new PaymentNotFoundException( messageSource.getMessage("not.found.payments.list",null,
                    "결제한 내역이 없습니다.",
                    Locale.getDefault()));
        }

        return responseDtoList;
    }
}
