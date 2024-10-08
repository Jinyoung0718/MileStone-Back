package com.sjy.milestone.review.entity;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.product.entity.Product;
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
    public Review(Member member, Product product, String content) {
        this.member = member;
        this.product = product;
        this.content = content;
    }
}