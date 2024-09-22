package com.sjy.milestone.chat.controller;

import com.sjy.milestone.chat.service.ChatMessageService;
import com.sjy.milestone.chat.dto.chatdto.ChatContentDTO;
import com.sjy.milestone.chat.dto.chatdto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/message")
public class ChatMsgController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/{roomId}/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@PathVariable String roomId, @RequestBody ChatContentDTO chatContentDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatMessageDTO chatMessageDTO = chatMessageService.sendMessage(roomId, userEmail, chatContentDTO);
        return ResponseEntity.ok(chatMessageDTO);
    }

    @GetMapping("/{roomId}/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable String roomId) {
        List<ChatMessageDTO> chatHistory = chatMessageService.getChatHistory(roomId);
        return ResponseEntity.ok(chatHistory);
    }
}