package com.sjy.milestone.board.repository;

import com.sjy.milestone.board.entity.Board;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @NotNull
    Page<Board> findAll(@NotNull Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 추가
    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.member m " +
            "LEFT JOIN FETCH b.comments c " +
            "LEFT JOIN FETCH c.member cm " +
            "WHERE b.id = :boardId")
    Optional<Board> findWithMemberAndCommentsById(@Param("boardId") Long boardId);

    @Query("SELECT b.member.userEmail FROM Board b WHERE b.id = :boardId")
    Optional<String> findAuthorEmailById(@Param("boardId") Long boardId);

    void deleteById(@NotNull Long boardId);

    @Query("SELECT b FROM Board b WHERE LOWER(CONCAT(b.title, ' ', b.content)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);
}