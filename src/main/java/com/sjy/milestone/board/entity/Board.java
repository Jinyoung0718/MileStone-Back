package com.sjy.milestone.board.entity;

import com.sjy.milestone.comment.entity.Comment;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import com.sjy.milestone.auth.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "BOARDS")
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "BOARD_TITLE", nullable = false) @Setter
    private String title;

    @Column(name = "BOARD_CONTENT", nullable = false, columnDefinition = "TEXT") @Setter
    private String content;

    @Column(name = "BOARD_VIEWCOUNT")
    private Integer viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    // orphanRemoval: comments 리스트에서 comments 객체를 제거하면 해당 객체는 DB 에서 삭제됨,
    // 다대일에서 일이 부모테이블이다. 왜냐하면 다를 많이 수용하고 있기에 부모테이블 취급한다

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    // 처음에 EAGER 설정이였지만, 게시물 목록 조회시, 댓글이 필요하지 않은 상황임에도 불구하고 댓글과 작성자 조회 쿼리가 나와서 고민이였으나
    // LAZY 설정으로 불필요한 데이터 로드를 막고, 필요한 경우에 쿼리를 이끌어가기 위해 함

    @Builder
    protected Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0;
    }

    public DetailBoardDTO toDetailDTO() {
        return DetailBoardDTO.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .authorEmail(this.getMember().getUserEmail())
                .createdAt(this.getCreatedAt())
                .viewCount(this.getViewCount())
                .comments(this.getComments().stream().map(Comment::toDTO).collect(Collectors.toList()))
                .build();
    }

    public MenuBoardDTO toMenuDTO() {
        return MenuBoardDTO.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .totalView(this.viewCount)
                .build();
    }

    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0;
        }

        this.viewCount++;
    }
}

// 지연 로딩은 객체가 필요할 때까지 데이터를 가져오지 않는 방식으로 객체가 처음 실행 시 최소한의 데이터만 로드한다
// 객체의 특정 필드나 연관된 엔티티를 실제로 접근시 해당 데이터를 DB 에서 가져온다
// 지연 로딩 사용이유는 성능최적화와 불필요한 데이터를 미리 로드하지 않기 위함이다.
// 프록시: 실제 데이터가 로드될 때까지 해당 필드의 |접근을 대리| 하며 필드에 접근하는 순간,
// ORM 프레임워크는 데이터베이스 쿼리를 실행하여 실제 데이터를 로드한다


// DTO 는 계층 간 데이터 전송을 위해 사용되며, 클라이언트로부터 받은 데이터를 엔티티로 변환하여 데이터베이스에 저장한다.
// 이 과정을 통해 데이터 전송과 데이터베이스 상호작용을 분리하고, 보안과 유지보수를 용이하게 한다. 즉 -> JPA_REPOSITORY 는 DTO 를 엔티티에서 저장하게 하는 것, 기본적으로는 데이터 통로 역할.