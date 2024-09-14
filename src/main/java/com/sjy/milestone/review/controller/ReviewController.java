package com.sjy.milestone.review.controller;

import com.sjy.milestone.review.ReviewService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.review.dto.ReviewDTO;
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

