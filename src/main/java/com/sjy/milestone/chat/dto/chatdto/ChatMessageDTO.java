package com.sjy.milestone.chat.dto.chatdto;

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
}