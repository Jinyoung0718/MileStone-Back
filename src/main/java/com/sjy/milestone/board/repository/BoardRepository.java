package com.sjy.milestone.board.repository;

import com.sjy.milestone.board.entity.Board;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @NotNull
    Page<Board> findAll(@NotNull Pageable pageable);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.member m " +
            "LEFT JOIN FETCH b.comments c " +
            "LEFT JOIN FETCH c.member cm " +
            "WHERE b.id = :boardId")
    Optional<Board> findWithMemberAndCommentsById(@Param("boardId") Long boardId);


    @Query("SELECT b.member.userEmail FROM Board b WHERE b.id = :boardId")
    Optional<String> findAuthorEmailById(@Param("boardId") Long boardId);

    // 주인 이메일만 필요한 상황이라 게시물의 작성자 이메일만 조회하는 쿼리를 작성,
    // 불필요한 데이터를 로드하지 않도록 하기 위함

    void deleteById(@NotNull Long boardId);

    // @Query("...")
    //Page<Board> searchBoard(String text, @NotNull Pageable pageable);

    Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
}

