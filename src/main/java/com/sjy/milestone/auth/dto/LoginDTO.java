package com.sjy.milestone.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoginDTO {

    @Email(message = "잘못된 이메일 형식입니다")
    @NotEmpty(message = "이메일은 필수 항목입니다")
    private String userEmail;

    @NotBlank @NotEmpty(message = "패스워드는 필수 항목입니다")
    private String userPassword;
}
