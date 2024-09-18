package com.sjy.milestone.account.dto.AuthDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhoneAuthVerifyDTO  {
    @NotBlank(message = "휴대전화 번호는 필수 입력값입니다")
    private String phoneNumber;

    @NotBlank(message = "인증번호 값은 필수입니다")
    private String verificationCode;
}