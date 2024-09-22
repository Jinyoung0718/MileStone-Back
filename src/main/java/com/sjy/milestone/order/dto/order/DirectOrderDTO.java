package com.sjy.milestone.order.dto.order;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DirectOrderDTO {
    private Long productOptionId;
    private int quantity;
    private String productName;
    private Long price;
    private String productImg;
}