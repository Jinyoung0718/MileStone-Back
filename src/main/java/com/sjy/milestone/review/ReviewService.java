package com.sjy.milestone.review;

import com.sjy.milestone.exception.notfound.ProductNotFoundException;
import com.sjy.milestone.exception.notfound.ReviewNotFoundException;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.product.repository.ProductRepository;
import com.sjy.milestone.review.repository.ReviewRepository;
import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.review.dto.ReviewDTO;
import com.sjy.milestone.review.entity.Review;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.account.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public ReviewDTO createReview(ReviewDTO reviewDTO, String userEmail) {
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾지 못했습니다"));

        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);

        Review review = reviewDTO.toEntity(product, member);
        Review saveReview = reviewRepository.save(review);
        return saveReview.toDTO();
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다"));

        if (!review.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("업데이트를 할 권한이 없습니다");
        }

        review.setContent(reviewDTO.getContent());
        Review updateReview = reviewRepository.save(review);
        return updateReview.toDTO();
    } // 만약 DTO 가 생성되거나 업데이트되는 과정에서 ID가 설정되지 않을 위험을 방지하기 위함

    public void deleteReview(Long reviewId, String userEmail) {
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다"));

        if (!findReview.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("삭제할 권한이 없습니다");
        }
        reviewRepository.deleteById(reviewId);
    }
}
