package Milestone.spring_project.backend.domain.Entity.CommentAndReview;

import Milestone.spring_project.backend.domain.DTO.ProductDTO.ReviewDTO;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REVIEWS")
@Getter
@NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Review.withMemberAndProduct",
                attributeNodes = {
                        @NamedAttributeNode("member"),
                        @NamedAttributeNode("product")
                }
        )
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;

    @Setter
    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Builder
    public Review(Long id, Member member, String content, Product product) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.product = product;
    }

    public ReviewDTO toDTO() {
        return ReviewDTO.builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .memberEmail(this.member.getUserEmail())
                .memberId(this.member.getId())
                .productId(this.product.getId())
                .build();
    }
}