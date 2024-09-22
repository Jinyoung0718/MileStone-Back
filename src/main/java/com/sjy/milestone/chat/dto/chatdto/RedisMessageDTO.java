package com.sjy.milestone.chat.dto.chatdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RedisMessageDTO {
    private String roomId;
    private String senderEmail;
    private String recipientEmail;
    private String message;
}
