package com.sjy.milestone.order.entity;

public enum OrderStatus {
    PENDING, // 대기 중
    ORDERED, // 주문 완료
    PROCS, // 주문 처리 중
    SHIPPED, // 배송 중
    DELIVERED, // 배송 완료
    CANCELLED, // 주문 취소
}