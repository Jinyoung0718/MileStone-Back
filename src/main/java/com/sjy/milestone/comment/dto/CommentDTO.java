package com.sjy.milestone.comment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentDTO {

    private Long commentId;
    private String content;
    private String authorEmail;
    private Long parentCommentId;
    private Long boardId;
    private LocalDateTime createdAt;
    private List<CommentDTO> childComments;
}
