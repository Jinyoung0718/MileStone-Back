package com.sjy.milestone.board.controller;

import com.sjy.milestone.board.BoardService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final SessionManager sessionManager;

    @GetMapping
    public Page<MenuBoardDTO> getBoards(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "7") int size,
                                        @RequestParam String sort) {
        return boardService.getBoards(page, size, sort);
    }

    @GetMapping("/{boardId}")
    public DetailBoardDTO getBoardById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    @PostMapping
    public ResponseEntity<DetailBoardDTO> createBoard(@RequestBody DetailBoardDTO detailBoardDTO,
                                                      @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionid) {
        String userEmail = sessionManager.getSession(sessionid);
        DetailBoardDTO createdBoard = boardService.createBoard(detailBoardDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId,
                                              @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionid) {
        String userEmail = sessionManager.getSession(sessionid);
        boardService.deleteBoard(boardId, userEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<DetailBoardDTO> updateBoard(@PathVariable Long boardId,
                                                      @RequestBody DetailBoardDTO detailBoardDTO,
                                                      @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        DetailBoardDTO updatedBoard = boardService.updateBoard(boardId, detailBoardDTO, userEmail);
        return ResponseEntity.ok().body(updatedBoard);
        // 리다이렉트 요구사항 : 작성한 게시판 본문
    }

    @GetMapping("/search")
    public Page<MenuBoardDTO> searchBoards(@RequestParam String query,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "7") int size,
                                           @RequestParam String sort) {
        return boardService.searchBoards(query, page, size, sort);
    }
} // 삭제 후 리다이렉트는 리액트에서 처리하는 것이 RestFul API 원칙에 더 적합하다.