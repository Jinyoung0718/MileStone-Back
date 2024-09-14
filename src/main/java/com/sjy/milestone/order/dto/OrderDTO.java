package com.sjy.milestone.order.dto;

import com.sjy.milestone.order.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderDTO {
    private Long id;
    // OrderDTO의 id 필드는 데이터베이스에서 주문을 조회하거나 수정 및 삭제에 이용

    private OrderStatus status;
    private String merchantUid;
    private Long totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;
    private String userEmail;
    private String requestMessage;
    private String recipientName;
    private String deliveryAddress;
    private String deliveryDetail;
    private String phoneNumber;

    public Long calculateTotalAmount() {
        return orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}

// MapToDouble 이란? -> 스트림 내의 각 요소를 double 타입으로 매핑하는 작업을 수행
// OrderDTO 는 하나의 주문에 대한 전체적인 정보를 제공하며 주문에 포함된 모든 주문 항목을 포함할 목적으로 생성 -> OrderItemDTO 는 각 주문 항목의 세부 정보를 나타낼 때 사용 예정
// 즉 OrderDTO 는 주문 목록, OrderItemDTO 는 주문 세부 항목 표시 때 사용 예정