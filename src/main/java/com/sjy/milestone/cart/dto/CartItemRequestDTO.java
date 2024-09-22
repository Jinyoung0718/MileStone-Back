package com.sjy.milestone.cart.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemRequestDTO {
    private Long productOptionId;
    private int quantity;
}