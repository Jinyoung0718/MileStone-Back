package Milestone.spring_project.backend.Controller.itemPage;

import Milestone.spring_project.backend.Service.itemService.ReviewService;
import Milestone.spring_project.backend.Util.Sesssion.SessionManager;
import Milestone.spring_project.backend.Util.Sesssion.SesssionConst;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final SessionManager sessionManager;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        ReviewDTO createdReview = reviewService.createReview(reviewDTO, userEmail);
        return ResponseEntity.status(201).body(createdReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        ReviewDTO updateReview = reviewService.updateReview(reviewId, reviewDTO, userEmail);
        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        reviewService.deleteReview(reviewId, userEmail);
        return ResponseEntity.noContent().build();
    }
}

