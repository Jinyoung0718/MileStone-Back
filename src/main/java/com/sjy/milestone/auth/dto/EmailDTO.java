package com.sjy.milestone.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmailDTO {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일은 형식이여야 합니다")
    private String email;
}
