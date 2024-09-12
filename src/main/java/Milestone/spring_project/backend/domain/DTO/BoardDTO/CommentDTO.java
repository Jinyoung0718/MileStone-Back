package Milestone.spring_project.backend.domain.DTO.BoardDTO;

import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Board;
import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Comment;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class CommentDTO {

    private Long commentId;
    private String content;
    private String authorEmail;
    private Long parentCommentId;
    private Long boardId;
    private LocalDateTime createdAt;
    private List<CommentDTO> childComments;

    public Comment toEntity(Board board, Member member, Comment parentComment) {
        return Comment.builder()
                .content(this.content)
                .board(board)
                .member(member)
                .parentComment(parentComment)
                .build();
    }
}
