package Milestone.spring_project.backend.domain.DTO.MemberDTO;

import Milestone.spring_project.backend.domain.Entity.Auth.MemberStatus;
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
