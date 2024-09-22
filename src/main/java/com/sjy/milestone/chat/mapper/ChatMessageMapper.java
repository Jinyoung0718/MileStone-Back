package com.sjy.milestone.chat.mapper;

import com.sjy.milestone.chat.dto.chatdto.ChatMessageDTO;
import com.sjy.milestone.chat.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(source = "chatRoom.id", target = "roomId")
    @Mapping(source = "sender.userEmail", target = "senderEmail")
    ChatMessageDTO toDTO(ChatMessage chatMessage);
}
