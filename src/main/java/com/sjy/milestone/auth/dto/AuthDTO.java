package com.sjy.milestone.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AuthDTO {
    private String phoneNumber;
    private String verificationCode;
}
