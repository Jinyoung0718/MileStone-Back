package com.sjy.milestone.comment.controller;

import com.sjy.milestone.comment.CommentService;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.comment.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long boardId, @RequestBody  CommentDTO commentDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentDTO createdCmtDTO = commentService.createComment(boardId, commentDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCmtDTO);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(commentId, userEmail);
        return ResponseEntity.noContent().build();
    }
}