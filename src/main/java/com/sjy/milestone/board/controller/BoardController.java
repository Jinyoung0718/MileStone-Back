package com.sjy.milestone.board.controller;

import com.sjy.milestone.board.BoardService;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DetailBoardDTO> createBoard(@RequestBody DetailBoardDTO detailBoardDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        DetailBoardDTO createdBoard = boardService.createBoard(detailBoardDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boardService.deleteBoard(boardId, userEmail);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{boardId}")
    public ResponseEntity<DetailBoardDTO> updateBoard(@PathVariable Long boardId, @RequestBody DetailBoardDTO detailBoardDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        DetailBoardDTO updatedBoard = boardService.updateBoard(boardId, detailBoardDTO, userEmail);
        return ResponseEntity.ok().body(updatedBoard);
    }

    @GetMapping("/search")
    public Page<MenuBoardDTO> searchBoards(@RequestParam String query,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "7") int size,
                                           @RequestParam String sort) {
        return boardService.searchBoards(query, page, size, sort);
    }
}