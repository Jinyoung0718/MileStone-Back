package com.sjy.milestone.order.dto;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.order.entity.OrderItem;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String productName;
    private ProductOptionDTO productOption;
    private int quantity;
    private String productImg;
    private Long price;

    public OrderItem toEntity(Product product, ProductOption productOption) {
        return OrderItem.builder()
                .product(product)
                .productOption(productOption)
                .quantity(this.quantity)
                .price(this.price)
                .build();
    }
}

// OrderItemDTO가 ProductOptionDTO를 포함하는 이유는 OrderItem이 ProductOption과의 관계를 가지고 있기 때문입니다.
// OrderItem 엔티티가 ProductOption 엔티티를 참조하듯이, OrderItemDTO도 ProductOptionDTO를 참조해야 합니다.