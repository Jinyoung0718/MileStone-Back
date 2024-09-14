package com.sjy.milestone.chat.service;

import com.sjy.milestone.chat.repository.ChatMessageRepository;
import com.sjy.milestone.chat.repository.ChatRoomRepository;
import com.sjy.milestone.auth.repository.MemberRepository;
import com.sjy.milestone.chat.dto.ChatContentDTO;
import com.sjy.milestone.chat.dto.ChatMessageDTO;
import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.chat.entity.ChatMessage;
import com.sjy.milestone.chat.entity.ChatRoom;
import com.sjy.milestone.chat.entity.ChatRoomStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public ChatMessageDTO sendMessage(String roomId, String senderEmail, ChatContentDTO chatContentDTO) {
        String content = chatContentDTO.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용이 비어 있습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        if (chatRoom.getStatus() != ChatRoomStatus.ACTIVE) {
            throw new IllegalStateException("채팅방이 비활성화 상태입니다");
        }

        Member sender = memberRepository.findByUserEmail(senderEmail);

        Member recipient = chatRoom.getAdmin().getUserEmail().equals(senderEmail) ?
                chatRoom.getUser() : chatRoom.getAdmin();

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        String jsonMessage = "{\"roomId\":\"" + roomId + "\", \"senderEmail\":\"" + senderEmail + "\", \"recipientEmail\":\"" + recipient.getUserEmail() + "\", \"content\":\"" + content + "\"}";
        redisTemplate.convertAndSend("chat/room/" + roomId, jsonMessage);
        // 객체를 직렬화를 통해서 클라이언트가 객체로 받게끔 하자

        return ChatMessageDTO.fromEntity(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getChatHistory(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndStatus(roomId, ChatRoomStatus.CLOSED)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return  chatRoom.getMessages().stream().map(ChatMessageDTO::fromEntity).collect(Collectors.toList());
    }
}