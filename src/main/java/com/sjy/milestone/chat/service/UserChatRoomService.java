package com.sjy.milestone.chat.service;

import com.sjy.milestone.Exception.UnauthorizedException;
import com.sjy.milestone.chat.repository.ChatRoomRepository;
import com.sjy.milestone.auth.repository.MemberRepository;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.chat.dto.ChatRoomDTO;
import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.auth.MemberStatus;
import com.sjy.milestone.chat.entity.ChatRoom;
import com.sjy.milestone.chat.entity.ChatRoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class UserChatRoomService {

    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final WebsocketSessionManager websocketSessionManager;

    @Transactional
    public String requestChatRoom(String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail);
        if (member.getStatus().equals(MemberStatus.ADMIN)) {
            throw new UnauthorizedException("어드민은 신청 불가합니다");
        }
        String roomId = UUID.randomUUID().toString();
        String key = "chat/request/" + roomId;
        redisTemplate.opsForHash().put(key, "userEmail", userEmail);
        redisTemplate.opsForHash().put(key, "status", "PENDING");

        List<Member> admins = memberRepository.findAllByStatus(MemberStatus.ADMIN);
        for (Member admin : admins) {
            String requestJsonMessage = "{\"type\":\"request\", \"message\":\"" + userEmail + "에게 상담 요청\", \"senderEmail\":\"" + userEmail + "\"}";
            websocketSessionManager.sendMessageToMember("ws/chat/notifications", admin.getUserEmail(), requestJsonMessage);
        }
        return roomId;
    }


    @Transactional
    public void cancelChatRoom(String roomId) {
        String key = "chat/request/" + roomId;
        String userEmail = (String) redisTemplate.opsForHash().get(key, "userEmail");
        redisTemplate.delete(key);

        List<Member> admins = memberRepository.findAllByStatus(MemberStatus.ADMIN);
        for (Member admin : admins) {
            String cancelJsonMessage = "{\"type\":\"cancel\", \"message\":\"" + userEmail + "의 상담 요청이 취소되었습니다\", \"senderEmail\":\"" + userEmail + "\"}";
            websocketSessionManager.sendMessageToMember("ws/chat/notifications", admin.getUserEmail(), cancelJsonMessage);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDTO> getUserChatRooms(String userEmail) {
        Member user = memberRepository.findByUserEmail(userEmail);

        List<ChatRoom> closedChatRooms = chatRoomRepository.findByUserAndStatus(user, ChatRoomStatus.CLOSED);

        return closedChatRooms.stream()
                .map(ChatRoomDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteChatRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        chatRoomRepository.delete(chatRoom);
    }
}
