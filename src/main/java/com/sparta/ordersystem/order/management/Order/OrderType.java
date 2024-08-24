package com.sparta.ordersystem.order.management.Order;

//주문의 상태
public enum OrderType {
    create, // 생성시
    running,//주문 확정 후
    cancel //주문 취소
}
