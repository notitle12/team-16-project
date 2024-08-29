package com.sparta.ordersystem.order.management.Payment;

import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Payment.dto.CreatePaymentRequestDto;
import com.sparta.ordersystem.order.management.Payment.dto.PaymentResponseDto;
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

    @Test
    @DisplayName("결제 내역 상세 조회 - 성공케이스")
    void testSuccessGetPayment(){
        UUID paymentId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .order(Order.builder().orderId(orderId).build())
                .build();

        given(paymentRepository.findById(paymentId)).willReturn(Optional.of(payment));

        PaymentResponseDto dto = paymentService.getPaymentsInDetail(paymentId);

        assertEquals(paymentId, dto.getPaymentId());
        assertEquals(orderId, dto.getOrderId());
    }

    @Test
    @DisplayName("결제 내역 상세 조회 - 존재하지 않는 결제 ID 실패케이스")
    void testErrorGetPaymentNotExistedPaymentId(){
        UUID paymentId = UUID.randomUUID();
        String expectedMessage = "존재하지 않는 결제 ID입니다.";

        given(paymentRepository.findById(paymentId)).willReturn(Optional.empty());
        given(messageSource.getMessage("not.found.payment.id",new UUID[]{paymentId},
                "존재하지 않는 결제 ID입니다.",
                Locale.getDefault())).willReturn(expectedMessage);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> paymentService.getPaymentsInDetail(paymentId));

        assertEquals(expectedMessage, exception.getMessage());
    }
}