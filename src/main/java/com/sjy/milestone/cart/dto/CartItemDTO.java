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
}
