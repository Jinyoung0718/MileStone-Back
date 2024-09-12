package Milestone.spring_project.backend.Service.ChatService;

import Milestone.spring_project.backend.Repository.ChatRoomRepository;
import Milestone.spring_project.backend.Repository.MemberRepository;
import Milestone.spring_project.backend.Util.Sesssion.WebsocketSessionManager;
import Milestone.spring_project.backend.domain.DTO.ChatDTO.ChatRoomDTO;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Auth.MemberStatus;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoom;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.*;

@Service @RequiredArgsConstructor
public class AdminChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final WebsocketSessionManager websocketSessionManager;
    private static final String LOCK_KEY_PREFIX = "lock/chat/request/";
    // 공부한 내용 : 당연한 거지만 static final 을 전역변수로 선언하여야지 한 번만 메모리 공간을 지정하지, 메서드에 지정하면 실행 시마다 지정하기 효율이 극악

    @Transactional
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

            String notificationJsonMessage = "{\"message\":\"" + admin.getUserEmail() + "님이 상담 해드립니다\", \"roomId\":\"" + roomId + "\", \"recipientEmail\":\"" + user.getUserEmail() + "\"}";
            websocketSessionManager.sendMessageToMember("ws/chat/notifications", user.getUserEmail(), notificationJsonMessage);
            // 사용자와 관리자는 관리자가 요청에 응답하는 순간을 잡기위해, 소켓을 날리고, 그 후 동시에 채팅방에 들어가기 위해 따로 소켓을 이용한다
            String jsonMessage = "{\"message\":\"관리자가 상담 요청을 수락하였습니다\", \"roomId\":\"" + roomId + "\", \"recipientEmail\":\"" + userEmail + "\"}";
            redisTemplate.convertAndSend("chat/room/" + roomId, jsonMessage);
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Transactional
    public void endChatRoom(String roomId, String adminEmail) {
        String key = "chat/request/" + roomId;
        String userEmail = (String) redisTemplate.opsForHash().get(key, "userEmail");
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        Member admin = memberRepository.findByUserEmail(adminEmail);
        if (admin.getStatus() != MemberStatus.ADMIN) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        chatRoom.setStatus(ChatRoomStatus.CLOSED);
        chatRoomRepository.save(chatRoom);

        String jsonMessage = "{\"message\":\"관리자가 채팅을 종료하였습니다\", \"roomId\":\"" + roomId + "\", \"recipientEmail\":\"" + userEmail + "\"}";
        redisTemplate.convertAndSend("chat/room/" + roomId, jsonMessage);
        redisTemplate.delete(key);
    }

    @Transactional(readOnly = true)
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