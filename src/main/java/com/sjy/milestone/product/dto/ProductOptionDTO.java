package com.sjy.milestone.product.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductOptionDTO {
    private Long id;
    private Integer stockQuantity;
    private String color;
    private String size;
}
