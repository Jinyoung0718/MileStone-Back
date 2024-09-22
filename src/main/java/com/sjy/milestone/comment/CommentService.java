package com.sjy.milestone.comment;

import com.sjy.milestone.comment.mapper.CommentMapper;
import com.sjy.milestone.exception.notfound.BoardNotFoundException;
import com.sjy.milestone.exception.notfound.CommentNotFoundException;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.board.repository.BoardRepository;
import com.sjy.milestone.comment.repository.CommentRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.comment.dto.CommentDTO;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.comment.entity.Comment;
import com.sjy.milestone.account.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentMapper commentMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public CommentDTO createComment(Long boardId, CommentDTO commentDTO, String userEmail) {

        Comment parentComment = null;
        if (commentDTO.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentDTO.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글을 찾을 수 없습니다"));
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        Member member = memberRepository.findByUserEmail(userEmail);
        Comment comment = commentMapper.toEntity(commentDTO, board, member, parentComment);
        Comment savedComment = commentRepository.save(comment);

        if (parentComment != null) {
            String parentEmail = parentComment.getMember().getUserEmail();
            redisTemplate.convertAndSend("comment/notice",parentEmail + ": 새로운 대댓글이 작성되었습니다");
        }

        return commentMapper.toDTO(savedComment);
    }

    public void deleteComment(Long commentId,String userEmail) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다"));

        if (!findComment.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다");
        }
        commentRepository.deleteById(commentId);
    }
}