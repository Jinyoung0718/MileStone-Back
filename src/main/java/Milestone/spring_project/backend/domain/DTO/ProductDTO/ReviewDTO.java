package Milestone.spring_project.backend.domain.DTO.ProductDTO;

import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Review;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
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