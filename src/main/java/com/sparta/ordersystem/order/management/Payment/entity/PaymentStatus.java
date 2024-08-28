package com.sparta.ordersystem.order.management.Payment.entity;

public enum PaymentStatus {
    RUNNING, //결제 진행 중
    COMPLETED,//완료
    FAILED, //결제 실패
    REFUNDED //환불 완료
}
