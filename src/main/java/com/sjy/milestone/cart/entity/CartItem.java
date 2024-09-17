package com.sjy.milestone.cart.entity;

import com.sjy.milestone.cart.dto.CartItemDTO;
import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CART_ITEMS")
@Getter @NoArgsConstructor
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ITEM_ID")
    private Long id;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption productOption;

    @Builder
    public CartItem(int quantity, Member member, ProductOption productOption) {
        this.member = member;
        this.productOption = productOption;
        this.quantity = quantity;
    } // RequestDTO 즉 장바구니 추가 시 넘어오는 DTO 를 바탕으로 엔티티에 저장하기 위해 생성


    public CartItemDTO toDTO() {
        return CartItemDTO.builder()
                .id(this.id)
                .quantity(this.quantity)
                .productName(this.productOption.getProduct().getName())
                .productOption(this.productOption.toDTO())
                .productImg(this.productOption.getProduct().getProductImg1())
                .build();
    }

    public TempOrderItemDTO toTempOrderItemDTO() {
        return TempOrderItemDTO.builder()
                .productOption(this.getProductOption().toDTO())
                .quantity(this.quantity)
                .price(this.productOption.getProduct().getPrice())
                .productImg(this.productOption.getProduct().getProductImg1())
                .build();
    }
}
