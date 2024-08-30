package com.sparta.ordersystem.order.management.Order.entity;

//주문의 상태
public enum OrderStatus {
    CREATE, // 생성시
    RUNNING,//주문 확정 후
    COMPLETED, //주문 완료
    CANCEL //주문 취소
}
