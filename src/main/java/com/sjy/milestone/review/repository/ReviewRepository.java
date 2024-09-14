package com.sjy.milestone.review.repository;

import com.sjy.milestone.review.entity.Review;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(value = "Review.withMemberAndProduct", type = EntityGraph.EntityGraphType.LOAD) @NotNull
    Optional<Review> findById(@NotNull Long id);
}