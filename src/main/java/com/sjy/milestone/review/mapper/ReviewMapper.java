package com.sjy.milestone.review.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.review.dto.ReviewDTO;
import com.sjy.milestone.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "memberEmail", source = "member.userEmail")
    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "productId", source = "product.id")
    ReviewDTO toReviewDTO(Review review);

    @Mapping(target = "content", source = "reviewDTO.content")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "member", source = "member")
    Review toReview(ReviewDTO reviewDTO, Product product, Member member);
}
