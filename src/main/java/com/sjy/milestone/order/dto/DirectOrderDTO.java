package com.sjy.milestone.order.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DirectOrderDTO {
    private Long productOptionId;
    private int quantity;
    private String productName;
    private Long price;
    private String productImg;
}

// DirectOrderDTO를 통해 생성된 OrderItem을 단일 항목으로 처리하기 위함 (바로 주문하기)