package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Review;
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