package com.sjy.milestone.order.dto.order;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String productName;
    private ProductOptionDTO productOption;
    private int quantity;
    private String productImg;
    private Long price;
}