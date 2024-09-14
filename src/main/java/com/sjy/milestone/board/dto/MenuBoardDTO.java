package com.sjy.milestone.board.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuBoardDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Integer totalView;
}
