package com.sjy.milestone.chat.mapper;

import com.sjy.milestone.chat.dto.chatdto.ChatRoomDTO;
import com.sjy.milestone.chat.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    @Mapping(target = "roomId", source = "id")
    @Mapping(target = "userEmail", source = "user.userEmail")
    @Mapping(target = "adminEmail", source = "admin.userEmail")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd")
    ChatRoomDTO toChatRoomDTO(ChatRoom chatRoom);
}

