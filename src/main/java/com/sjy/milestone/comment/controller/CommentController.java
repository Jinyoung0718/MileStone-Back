package com.sjy.milestone.comment.controller;

import com.sjy.milestone.comment.CommentService;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.comment.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long boardId, @RequestBody  CommentDTO commentDTO, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        CommentDTO createdCmtDTO = commentService.createComment(boardId, commentDTO, sessionId);
        return ResponseEntity.status(201).body(createdCmtDTO);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        commentService.deleteComment(commentId, sessionId);
        return ResponseEntity.noContent().build();
    }
}