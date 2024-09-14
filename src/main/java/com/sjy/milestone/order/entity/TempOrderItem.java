package com.sjy.milestone.order.entity;

import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEMP_ORDER_ITEMS")
@Getter @NoArgsConstructor
public class TempOrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_ORDER_ITEM_ID")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMP_ORDER_ID", nullable = false)
    private TempOrder tempOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption productOption;


    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Builder
    public TempOrderItem(ProductOption productOption, int quantity) {
        this.productOption = productOption;
        this.quantity = quantity;
    }

    public TempOrderItemDTO toDTO() {
        return TempOrderItemDTO.builder()
                .id(this.id)
                .quantity(this.quantity)
                .productOption(this.productOption.toDTO())
                .price(this.productOption.getProduct().getPrice())
                .productImg(this.productOption.getProduct().getProductImg1())
                .build();
    }
}
