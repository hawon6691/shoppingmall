package com.example.shoppingmall.domain;

public enum OrderStatus {
    PENDING,        // 결제 대기
    CONFIRMED,      // 주문 확인
    PROCESSING,     // 처리 중
    SHIPPED,        // 배송 중
    DELIVERED,      // 배송 완료
    CANCELLED       // 취소됨
}
