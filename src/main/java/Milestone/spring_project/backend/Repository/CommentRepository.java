package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
