package Milestone.spring_project.backend.domain.Entity.CommentAndReview;

import Milestone.spring_project.backend.domain.DTO.BoardDTO.CommentDTO;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "COMMENTS")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "PARENT_COMMENT_ID")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

//    한 엔티티 내부에서 계층 구조를 이룰 경우, 부모-자식 관계를 정의하기 위해 외래 키가 필요하며.
//    이 외래 키를 기본키를 참조하여 계층 구조를 형성한다 -> @JoinColumn은 단순 참조 기능에 더불어 데이터베이스 테이블에 외래 키 열을 생성하고 기본키를 참조한다

    @Builder
    public Comment(Long id, Member member, String content, Board board, Comment parentComment) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.board = board;
        this.parentComment = parentComment;
    }

    // childComments 필드가 빌더 생성자에 포함되지 않은 이유는 부모-자식 관계의 설정은 한 번의 생성자 호출로 처리되지 않기 때문
    // 즉 부모-자식 관계를 설정하는 것은 별도의 로직으로 처리해야 한다

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .authorEmail(comment.getMember().getUserEmail())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .boardId(comment.getBoard().getId())
                .createdAt(comment.getCreatedAt())
                .childComments(comment.getChildComments().stream()
                        .map(Comment::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}