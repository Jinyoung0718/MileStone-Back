package com.sjy.milestone.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjy.milestone.chat.dto.AdminRedisMessageDTO;
import com.sjy.milestone.chat.repository.ChatRoomRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.chat.dto.ChatRoomDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.chat.entity.ChatRoom;
import com.sjy.milestone.chat.entity.ChatRoomStatus;
import com.sjy.milestone.session.WebsocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.*;

@Service @RequiredArgsConstructor
public class AdminChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final WebsocketSessionManager websocketSessionManager;

    private static final String LOCK_KEY_PREFIX = "lock/chat/request/";

    @Transactional @PreAuthorize("hasRole('ADMIN')")
    public void acceptChatRoom(String roomId, String adminEmail) {
        String key = "chat/request/" + roomId;
        String lockKey = LOCK_KEY_PREFIX + roomId;

        boolean isLocked = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofSeconds(3)));
        if (!isLocked) {
            throw new IllegalStateException("다른 관리자가 이미 수락 중입니다");
        }

        try {
            String userEmail = (String) redisTemplate.opsForHash().get(key, "userEmail");
            if (userEmail == null) {
                throw new IllegalStateException("해당 요청을 찾을 수 없습니다.");
            }
            String status = (String) redisTemplate.opsForHash().get(key, "status");
            if ("ACCEPTED".equals(status)) {
                throw new IllegalStateException("다른 관리자가 이미 수락했습니다.");
            }

            redisTemplate.opsForHash().put(key, "status", "ACCEPTED");
            Member user = memberRepository.findByUserEmail(userEmail);
            Member admin = memberRepository.findByUserEmail(adminEmail);

            ChatRoom chatRoom = ChatRoom.acceptCreateActiveRoom(roomId, user, admin);
            chatRoomRepository.save(chatRoom);

            AdminRedisMessageDTO roomAcceptanceNotification  = new AdminRedisMessageDTO("관리자가 상담 요청을 수락하였습니다", roomId, userEmail);
            AdminRedisMessageDTO userNotification = new AdminRedisMessageDTO(admin.getUserEmail() + "님이 상담 해드립니다", roomId, user.getUserEmail());

            try {
                websocketSessionManager.sendMessageToMember("ws/chat/notifications", user.getUserEmail(), objectMapper.writeValueAsString(roomAcceptanceNotification ));
                redisTemplate.convertAndSend("chat/room/" + roomId, objectMapper.writeValueAsString(userNotification));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("메시지 직렬화 실패", e);
            }

        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Transactional @PreAuthorize("hasRole('ADMIN')")
    public void endChatRoom(String roomId) {
        String key = "chat/request/" + roomId;
        String userEmail = (String) redisTemplate.opsForHash().get(key, "userEmail");
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoom.setStatus(ChatRoomStatus.CLOSED);
        chatRoomRepository.save(chatRoom);

        AdminRedisMessageDTO adminRedisMessageDTO = new AdminRedisMessageDTO("관리자가 채팅을 종료하였습니다", roomId, userEmail);

        try {
            redisTemplate.convertAndSend("chat/room/" + roomId, objectMapper.writeValueAsString(adminRedisMessageDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 직렬화 실패", e);
        }

        redisTemplate.delete(key);
    }

    @Transactional(readOnly = true) @PreAuthorize("hasRole('ADMIN')")
    public List<ChatRoomDTO> getAdminChatRooms() {
        List<ChatRoomDTO> pendingChatRooms = new ArrayList<>();
        Cursor<byte[]> cursor = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection().scan(ScanOptions.scanOptions().match("chat/request/*").count(100).build());

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            String userEmail = (String) redisTemplate.opsForHash().get(key, "userEmail");
            String status = (String) redisTemplate.opsForHash().get(key, "status");

            if ("PENDING".equals(status)) {
                try {
                    ChatRoomDTO chatRoomDTO = ChatRoomDTO.fromRedis(
                            key.substring("chat/request/".length()), userEmail, ChatRoomStatus.valueOf(status)
                    );
                    pendingChatRooms.add(chatRoomDTO);
                } catch (Exception e) {
                    throw new IllegalStateException("상태가 요청 중이어야 합니다", e);
                }
            }
        }
        cursor.close();
        return pendingChatRooms;
    } // scan 이 안정적이고 keys 를 지양해야 하는 이유 블로그 첨부 (h_scan 대신에 scan 사용)
}