package com.sjy.milestone.cart.dto;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemDTO {
    private Long id;
    private int quantity;
    private String productName;
    private ProductOptionDTO productOption;
    private String productImg;
} // OrderItemDTO 와 똑같지만, 장바구니와 주문을 달리하기 위해 같은 필드여도 역할을 구분
