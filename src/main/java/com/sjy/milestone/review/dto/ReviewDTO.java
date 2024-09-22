package com.sjy.milestone.review.dto;

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
}