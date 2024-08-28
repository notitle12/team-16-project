package com.sparta.ordersystem.order.management.Payment;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentMethod;
import com.sparta.ordersystem.order.management.Payment.entity.PaymentStatus;
import com.sparta.ordersystem.order.management.Payment.repository.PaymentRepository;
import com.sparta.ordersystem.order.management.Payment.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 내격 저장 - 성공케이스")
    void testSuccessCreatePayment(){
        CreatePaymentRequestDto dto =CreatePaymentRequestDto.builder()
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .paymentStatus(PaymentStatus.RUNNING)
                .price(10000)
                .orderId(UUID.randomUUID())
                .build();
        Order order = Order.builder()
                .orderId(dto.getOrderId())
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(dto.getOrderId())).willReturn(Optional.of(order));

        // When
        paymentService.cretePayment(dto);

        // Then
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 내격 저장 - 주문 ID가 없는 경우")
    void testErrorCreatePaymentNotExistedOrderId(){
        // Given
        UUID orderId = UUID.randomUUID();
        CreatePaymentRequestDto dto = CreatePaymentRequestDto.builder()
                .orderId(orderId)
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.empty());
        given(messageSource.getMessage("not.found.order.id", new UUID[]{orderId}, "존재하지 않는 주문 ID", Locale.getDefault()))
                .willReturn("존재하지 않는 주문 ID");

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> paymentService.cretePayment(dto));
        // Then
        assertEquals("존재하지 않는 주문 ID", exception.getMessage());
    }
}