package com.sjy.milestone.product.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuProductDTO {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String categoryName;
    private String productImg;
}