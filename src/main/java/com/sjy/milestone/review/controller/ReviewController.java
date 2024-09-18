package com.sjy.milestone.review.controller;

import com.sjy.milestone.review.ReviewService;
import com.sjy.milestone.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewDTO createdReview = reviewService.createReview(reviewDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewDTO updateReview = reviewService.updateReview(reviewId, reviewDTO, userEmail);
        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewService.deleteReview(reviewId, userEmail);
        return ResponseEntity.noContent().build();
    }
}

