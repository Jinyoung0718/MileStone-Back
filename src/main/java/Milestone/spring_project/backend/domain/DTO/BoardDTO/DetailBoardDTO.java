package Milestone.spring_project.backend.domain.DTO.BoardDTO;

import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Board;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
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

    public Board toEntity(Member member) {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
                .build();
    }
    // toEntity 메서드는 DTO를 엔티티로 변환하여 데이터베이스에 저장할 때 사용한다.
    // DTO에 담긴 데이터를 엔티티 객체로 변환하여 엔티티에 정의된 기본값이나 자동 생성되는 필드들(예: id, createdAt)이 적용되도록 할 수 있다.
}
