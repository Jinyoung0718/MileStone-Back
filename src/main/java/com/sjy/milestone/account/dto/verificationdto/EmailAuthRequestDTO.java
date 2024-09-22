package com.sjy.milestone.account.dto.verificationdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailAuthRequestDTO {
    @NotBlank(message = "이메일은 필수 입력값입니다") @Email(message = "이메일 형식이여야 합니다")
    private String email;
}
