package com.sjy.milestone.cart.entity;

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
    public CartItem(Member member, ProductOption productOption, int quantity) {
        this.member = member;
        this.productOption = productOption;
        this.quantity = quantity;
    }
}
