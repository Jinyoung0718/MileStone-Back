package com.sjy.milestone.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjy.milestone.chat.dto.chatdto.RedisMessageDTO;
import com.sjy.milestone.chat.mapper.ChatMessageMapper;
import com.sjy.milestone.chat.repository.ChatMessageRepository;
import com.sjy.milestone.chat.repository.ChatRoomRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.chat.dto.chatdto.ChatContentDTO;
import com.sjy.milestone.chat.dto.chatdto.ChatMessageDTO;
import com.sjy.milestone.account.entity.Member;
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
    private final ChatMessageMapper chatMessageMapper;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

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

        try {
            RedisMessageDTO redisMessageDTO = new RedisMessageDTO(roomId, senderEmail, recipient.getUserEmail(), content);
            redisTemplate.convertAndSend("chat/room/" + roomId, objectMapper.writeValueAsString(redisMessageDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 직렬화 실패", e);
        }

        return chatMessageMapper.toDTO(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getChatHistory(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndStatus(roomId, ChatRoomStatus.CLOSED)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return  chatRoom.getMessages().stream().map(chatMessageMapper::toDTO).collect(Collectors.toList());
    }
}