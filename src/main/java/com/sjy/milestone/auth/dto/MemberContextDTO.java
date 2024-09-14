package com.sjy.milestone.auth.dto;

import com.sjy.milestone.auth.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MemberContextDTO {
    private String userEmail;
    private MemberStatus memberStatus;

    public static MemberContextDTO setDTO(String userEmail, MemberStatus memberStatus) {
        return new MemberContextDTO(userEmail, memberStatus);
    }
}
