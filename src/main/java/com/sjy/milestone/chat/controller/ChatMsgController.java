package com.sjy.milestone.chat.controller;

import com.sjy.milestone.chat.service.ChatMessageService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.chat.dto.ChatContentDTO;
import com.sjy.milestone.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/message")
public class ChatMsgController {

    private final ChatMessageService chatMessageService;
    private final SessionManager sessionManager;

    /**
     * 특정 채팅방에 메시지를 전송
     * @param roomId 채팅방 ID
     * @param sessionId 보내는 사람 식별
     * @Param content 메시지 내용
     * @return 전송된 메시지의 정보
     */

    @PostMapping("/{roomId}/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@PathVariable String roomId, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId, @RequestBody ChatContentDTO chatContentDTO) {
        String userEmail = sessionManager.getSession(sessionId);
        ChatMessageDTO chatMessageDTO = chatMessageService.sendMessage(roomId, userEmail, chatContentDTO);
        return ResponseEntity.ok(chatMessageDTO);
    }

    /**
     * 특정 채팅방의 메시지 기록을 가져옴
     * @param roomId 채팅방 ID
     * @return 채팅 메시지 목록
     */

    @GetMapping("/{roomId}/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable String roomId) {
        List<ChatMessageDTO> chatHistory = chatMessageService.getChatHistory(roomId);
        return ResponseEntity.ok(chatHistory);
    }
}