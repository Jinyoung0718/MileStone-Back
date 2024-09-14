package com.sjy.milestone.chat.dto;

import com.sjy.milestone.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private String roomId;
    private String senderEmail;
    private String content;
    private LocalDateTime sendAt;

    public static ChatMessageDTO fromEntity(ChatMessage chatMessage) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setId(chatMessage.getId());
        chatMessageDTO.setRoomId(chatMessage.getChatRoom().getId());
        chatMessageDTO.setSenderEmail(chatMessage.getSender().getUserEmail());
        chatMessageDTO.setContent(chatMessage.getContent());
        chatMessageDTO.setSendAt(chatMessage.getSendAt());
        return chatMessageDTO;
    }
}