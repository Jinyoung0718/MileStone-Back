package Milestone.spring_project.backend.Service.BoardService;

import Milestone.spring_project.backend.Exception.BoardNotFoundException;
import Milestone.spring_project.backend.Exception.CommentNotFoundException;
import Milestone.spring_project.backend.Exception.UnauthorizedException;
import Milestone.spring_project.backend.Repository.BoardRepository;
import Milestone.spring_project.backend.Repository.CommentRepository;
import Milestone.spring_project.backend.Repository.MemberRepository;
import Milestone.spring_project.backend.Util.Sesssion.SessionManager;
import Milestone.spring_project.backend.domain.DTO.BoardDTO.CommentDTO;
import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Board;
import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Comment;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
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
    private final SessionManager sessionManager;
    private final RedisTemplate<String, String> redisTemplate;

    // 특정한 컬렉션을 변형하거나 처리할 때 stream 을 사용하는 것 DB에 저장되어 있는 Comment 내용들을 DTO 에 할당한 뒤 리스트로 반환
    // stream 은 컬렉션 데이터를 함수형 스타일로 처리할 수 있게 해주며 데이터 변환(Map), 필터링(filter), 수집(collect), 반복처리(For each)

    public CommentDTO createComment(Long boardId, CommentDTO commentDTO, String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);

        Comment parentComment = null;
        if (commentDTO.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentDTO.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글을 찾을 수 없습니다"));
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        Member member = memberRepository.findByUserEmail(userEmail);
        Comment comment = commentDTO.toEntity(board, member, parentComment);
        Comment savedComment = commentRepository.save(comment);

        if (parentComment != null) {
            String parentEmail = parentComment.getMember().getUserEmail();
            redisTemplate.convertAndSend("comment/notice",parentEmail + ": 새로운 대댓글이 작성되었습니다");
        }
        return Comment.toDTO(savedComment);
    }

    public void deleteComment(Long commentId,String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다"));

        if (!findComment.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다");
        }
        commentRepository.deleteById(commentId);
    }
}