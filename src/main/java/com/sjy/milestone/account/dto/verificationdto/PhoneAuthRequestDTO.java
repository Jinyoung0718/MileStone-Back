package com.sjy.milestone.account.dto.verificationdto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhoneAuthRequestDTO {
    @NotBlank(message = "휴대전화 번호는 필수 입력값입니다")
    private String phoneNumber;
}
