package com.sjy.milestone.chat.controller;

import com.sjy.milestone.chat.service.AdminChatRoomService;
import com.sjy.milestone.chat.service.UserChatRoomService;
import com.sjy.milestone.chat.dto.ChatRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/chat")
public class ChatController {

    private final AdminChatRoomService adminChatRoomService;
    private final UserChatRoomService userChatRoomService;

    @PostMapping("/request")
    public ResponseEntity<String> requestChatRoom() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String roomId = userChatRoomService.requestChatRoom(userEmail);
        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/request/cancel/{roomId}")
    public ResponseEntity<Void> cancelChatRoom(@PathVariable String roomId) {
        userChatRoomService.cancelChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/request/{roomId}/accept")
    public ResponseEntity<Void> acceptChatRoom(@PathVariable String roomId) {
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        adminChatRoomService.acceptChatRoom(roomId, adminEmail);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/end/{roomId}")
    public ResponseEntity<Void> endChatRoom(@PathVariable String roomId) {
        adminChatRoomService.endChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getUserChatRooms() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
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