package com.sjy.milestone.account.dto.accountdto;

import com.sjy.milestone.account.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MemberContextDTO {
    private String userEmail;
    private MemberStatus memberStatus;
}
