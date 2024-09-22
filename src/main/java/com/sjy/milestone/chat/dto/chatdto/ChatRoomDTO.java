package com.sjy.milestone.chat.dto.chatdto;

import com.sjy.milestone.chat.entity.ChatRoomStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatRoomDTO {

    private String roomId;
    private String userEmail;
    private String adminEmail;
    private ChatRoomStatus status;
    private LocalDate createdAt;
}