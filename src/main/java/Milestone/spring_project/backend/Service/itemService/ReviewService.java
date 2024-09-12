package Milestone.spring_project.backend.Service.itemService;

import Milestone.spring_project.backend.Exception.ProductNotFoundException;
import Milestone.spring_project.backend.Exception.ReviewNotFoundException;
import Milestone.spring_project.backend.Exception.UnauthorizedException;
import Milestone.spring_project.backend.Repository.MemberRepository;
import Milestone.spring_project.backend.Repository.ProductRepository;
import Milestone.spring_project.backend.Repository.ReviewRepository;
import Milestone.spring_project.backend.Util.Validator.Member.MemberValidator;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.ReviewDTO;
import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Review;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
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
