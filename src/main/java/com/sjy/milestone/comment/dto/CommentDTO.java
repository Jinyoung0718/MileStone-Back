package com.sjy.milestone.comment.dto;

import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.comment.entity.Comment;
import com.sjy.milestone.auth.entity.Member;
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
