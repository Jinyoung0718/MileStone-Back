package com.sjy.milestone.board;

import com.sjy.milestone.Exception.BoardNotFoundException;
import com.sjy.milestone.Exception.UnauthorizedException;
import com.sjy.milestone.board.repository.BoardRepository;
import com.sjy.milestone.auth.repository.MemberRepository;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import com.sjy.milestone.auth.MemberStatus;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.auth.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<MenuBoardDTO> getBoards(int page, int size, String sort) {
        Pageable pageable;
        if (sort.equals("최신순")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        }
        return boardRepository.findAll(pageable).map(Board::toMenuDTO);
    }

    // page: 스프링 데이터 JPA 에서 페이징된 결과를 나타내는 인터페이스
    // pageable: 페이징 정보를 캡슐화하는 인터페이스
    // pageRequest: Pageable 구현체로 주로 페이지 (번호와 크기)를 설정하여 추가 페이징 요청을 생성할 수 있다

    public DetailBoardDTO getBoardById(Long boardId) {
        Board board = boardRepository.findWithMemberAndCommentsById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        board.incrementViewCount(); // 동시성
        boardRepository.save(board);
        return board.toDetailDTO();
    }

    public DetailBoardDTO createBoard(DetailBoardDTO detailBoardDTO, String userEmail) {
        checkAdminRights(userEmail);
        Member member = memberRepository.findByUserEmail(userEmail);
        detailBoardDTO.setAuthorEmail(userEmail);

        Board board = detailBoardDTO.toEntity(member);
        Board saveBoard = boardRepository.save(board);
        return saveBoard.toDetailDTO();
    }

    // 클라이언트로부터 받은 데이터를 서비스 계층으로 전달하기 위해서 DTO 를 매개변수로 받고
    // DTO 는 데이터 전송 객체이므로 엔티티와 직접적인 연관이 없으며. DTO 를 엔티티로 변환하여
    // 데이터베이스에 저장하는 것이 일반적인 패턴이다.

    // return 값을 엔티티에 담는 것도 클라이언트에게 응답으로 보내기 위해서다.

    public void deleteBoard(Long boardId, String userEmail) {
        checkAdminRights(userEmail);
        String authorEmail = boardRepository.findAuthorEmailById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        if (!authorEmail.equals(userEmail)) {
            throw new UnauthorizedException("게시물을 삭제할 권한이 없습니다");
        }

        boardRepository.deleteById(boardId);
    }

    // deleteBoard 기능은 사용자가 본인이 작성한 게시물 본문에서만 삭제버튼이 보이게 할 것 (리액트 처리)
    // 백엔드에서는 사용자가 게시물의 작성자인지 확인하는 로직만 추가

    public DetailBoardDTO updateBoard(Long boardId, DetailBoardDTO detailBoardDTO, String userEmail) {
        checkAdminRights(userEmail);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시물을 찾을 수 없습니다"));

        if (!board.getMember().getUserEmail().equals(userEmail)) {
            throw new UnauthorizedException("게시물을 수정할 권한이 없습니다");
        }

        board.setTitle(detailBoardDTO.getTitle());
        board.setContent(detailBoardDTO.getContent());

        Board updatedBoard = boardRepository.save(board);
        return updatedBoard.toDetailDTO();
    }

    public Page<MenuBoardDTO> searchBoards(String query, int page, int size, String sort) {
        Pageable pageable;
        if (sort.equals("최신순")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        }

        return boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query, pageable)
                .map(Board::toMenuDTO);
    }

    private void checkAdminRights(String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail);
        if (member.getStatus() != MemberStatus.ADMIN) {
            throw new UnauthorizedException("해당 작업을 수행할 권한이 없습니다.");
        }
    }
}

// 현재 코드가 DTO 를 엔티티에 저장하고, 엔티티에서 값을 다시 할당 받아 출력하는데 반복이 된다. -> 해결해야 할 과제