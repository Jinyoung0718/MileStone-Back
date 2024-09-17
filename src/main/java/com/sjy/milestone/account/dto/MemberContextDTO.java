package com.sjy.milestone.account.dto;

import com.sjy.milestone.account.MemberStatus;
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
