package com.sjy.milestone.order.dto.order;

import com.sjy.milestone.order.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderDTO {
    private Long id;
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
}