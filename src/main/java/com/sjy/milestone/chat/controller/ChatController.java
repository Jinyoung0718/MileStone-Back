package com.sjy.milestone.chat.controller;

import com.sjy.milestone.chat.service.AdminChatRoomService;
import com.sjy.milestone.chat.service.UserChatRoomService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.chat.dto.ChatRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/chat")
public class ChatController {

    private final AdminChatRoomService adminChatRoomService;
    private final UserChatRoomService userChatRoomService;
    private final SessionManager sessionManager;

    @PostMapping("/request")
    public ResponseEntity<String> requestChatRoom(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        String roomId = userChatRoomService.requestChatRoom(userEmail);
        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/request/cancel/{roomId}")
    public ResponseEntity<Void> cancelChatRoom(@PathVariable String roomId) {
        userChatRoomService.cancelChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/request/{roomId}/accept")
    public ResponseEntity<Void> acceptChatRoom(@PathVariable String roomId, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String adminEmail = sessionManager.getSession(sessionId);
        adminChatRoomService.acceptChatRoom(roomId, adminEmail);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/end/{roomId}")
    public ResponseEntity<Void> endChatRoom(@PathVariable String roomId, @CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String adminEmail = sessionManager.getSession(sessionId);
        adminChatRoomService.endChatRoom(roomId, adminEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getUserChatRooms(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        List<ChatRoomDTO> chatRooms = userChatRoomService.getUserChatRooms(userEmail);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/admin/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getAdminChatRooms() {
        List<ChatRoomDTO> adminChatRooms = adminChatRoomService.getAdminChatRooms();
        return ResponseEntity.ok(adminChatRooms);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId) {
        userChatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}