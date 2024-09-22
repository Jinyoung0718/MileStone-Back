package com.sjy.milestone.board.dto;

import com.sjy.milestone.comment.dto.CommentDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class DetailBoardDTO {

    private Long id;
    private String title;
    private String content;
    private String authorEmail;
    private LocalDateTime createdAt;
    private List<CommentDTO> comments;
    private Integer viewCount;
}
