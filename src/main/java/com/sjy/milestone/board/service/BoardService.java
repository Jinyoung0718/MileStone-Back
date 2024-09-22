package com.sjy.milestone.board.service;

import com.sjy.milestone.board.entity.BoardSorted;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import com.sjy.milestone.board.mapper.BoardMapper;
import com.sjy.milestone.exception.notfound.BoardNotFoundException;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.board.repository.BoardRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.account.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardMapper boardMapper;

    public Page<MenuBoardDTO> getBoards(int page, int size, String sort) {
        Pageable pageable = getPageable(page, size, sort);
        return boardRepository.findAll(pageable).map(boardMapper::toMenuBoardDTO);
    }

    // page: 스프링 데이터 JPA 에서 페이징된 결과를 나타내는 인터페이스
    // pageable: 페이징 정보를 캡슐화하는 인터페이스
    // pageRequest: Pageable 구현체로 주로 페이지 (번호와 크기)를 설정하여 추가 페이징 요청을 생성할 수 있다

    public DetailBoardDTO getBoardById(Long boardId) {
        Board board = boardRepository.findWithMemberAndCommentsById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        board.incrementViewCount();
        boardRepository.save(board);
        return boardMapper.toDetailBoardDTO(board);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DetailBoardDTO createBoard(DetailBoardDTO detailBoardDTO, String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail);
        detailBoardDTO.setAuthorEmail(userEmail);

        Board board = boardMapper.toEntity(detailBoardDTO, member);
        Board saveBoard = boardRepository.save(board);
        return boardMapper.toDetailBoardDTO(saveBoard);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBoard(Long boardId, String userEmail) {
        String authorEmail = boardRepository.findAuthorEmailById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        if (!authorEmail.equals(userEmail)) {
            throw new UnauthorizedException("게시물을 삭제할 권한이 없습니다");
        }

        boardRepository.deleteById(boardId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DetailBoardDTO updateBoard(Long boardId, DetailBoardDTO detailBoardDTO, String userEmail) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        if (!board.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("게시물을 수정할 권한이 없습니다");
        }

        board.setTitle(detailBoardDTO.getTitle());
        board.setContent(detailBoardDTO.getContent());

        Board updatedBoard = boardRepository.save(board);
        return boardMapper.toDetailBoardDTO(updatedBoard);
    }

    public Page<MenuBoardDTO> searchBoards(String query, int page, int size, String sort) {
        Pageable pageable = getPageable(page, size, sort);

        return boardRepository.findByTitleOrContent(query, pageable)
                .map(boardMapper::toMenuBoardDTO);
    }

    private Pageable getPageable(int page, int size, String sort) {
        BoardSorted boardSorted;

        try {
            boardSorted = BoardSorted.fromValue(sort);
        } catch (IllegalArgumentException e) {
            boardSorted = BoardSorted.LATEST;
        }

        if (boardSorted == BoardSorted.LATEST) {
            return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (boardSorted == BoardSorted.OLDEST) {
            return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}