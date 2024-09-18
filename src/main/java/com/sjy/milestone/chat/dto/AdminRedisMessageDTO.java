package com.sjy.milestone.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AdminRedisMessageDTO {
    private String message;
    private String roomId;
    private String recipientEmail;
}
