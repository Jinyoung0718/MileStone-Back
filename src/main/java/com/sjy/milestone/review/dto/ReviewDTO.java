package com.sjy.milestone.review.dto;

import com.sjy.milestone.review.entity.Review;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.auth.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class ReviewDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String memberEmail;
    private Long memberId;
    private Long productId;

    public Review toEntity(Product product, Member member) {
        return Review.builder()
                .id(this.id)
                .content(this.content)
                .member(member)
                .product(product)
                .build();
    }
}