package Milestone.spring_project.backend.Service.ChatService;

import Milestone.spring_project.backend.Repository.ChatMessageRepository;
import Milestone.spring_project.backend.Repository.ChatRoomRepository;
import Milestone.spring_project.backend.Repository.MemberRepository;
import Milestone.spring_project.backend.domain.DTO.ChatDTO.ChatContentDTO;
import Milestone.spring_project.backend.domain.DTO.ChatDTO.ChatMessageDTO;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatMessage;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoom;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoomStatus;
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

        return ChatMessageDTO.fromEntity(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getChatHistory(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndStatus(roomId, ChatRoomStatus.CLOSED)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return  chatRoom.getMessages().stream().map(ChatMessageDTO::fromEntity).collect(Collectors.toList());
    }
}