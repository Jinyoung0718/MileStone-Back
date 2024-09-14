package com.sjy.milestone.cart.dto;

import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.product.entity.ProductOption;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemRequestDTO {
    private Long productOptionId;
    private int quantity;

    public CartItem toEntity(Member member, ProductOption productOption) {
        return CartItem.builder()
                .member(member)
                .productOption(productOption)
                .quantity(this.quantity)
                .build();
    }
}


// RequestParameter는 폼 데이터 혹은 쿼리 문자열로 전달되는 변수인데 이러한 요청 파라미터를 묶어서
// DTO로 처리하는 것이 좋은 방법이라 습득하여 RequestDTO 따로 정의함 -> 유지보수 및 간결함