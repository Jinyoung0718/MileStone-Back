package com.sjy.milestone.order.dto.temp;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TempOrderItemDTO {

    private Long id;
    private int quantity;
    private ProductOptionDTO productOption;
    private Long price;
    private String productImg;
}
