package com.sjy.milestone.order.dto;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.order.entity.OrderItem;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TempOrderItemDTO {

    private Long id;
    private int quantity;
    private ProductOptionDTO productOption;
    private Long price;
    private String productImg;

    public OrderItem toEntity(Product product, ProductOption productOption) {
        return OrderItem.builder()
                .product(product)
                .productOption(productOption)
                .quantity(this.quantity)
                .price(this.price)
                .productImg(this.productImg)
                .build();
    }
}
