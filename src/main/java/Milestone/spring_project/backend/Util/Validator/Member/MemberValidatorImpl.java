package Milestone.spring_project.backend.Util.Validator.Member;

import Milestone.spring_project.backend.Exception.NotFindMember;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberValidatorImpl implements MemberValidator {

    @Override
    public void validateMember(Member member) {
        if (member == null) {
            throw new NotFindMember("회원 정보를 찾을 수 없습니다");
        }
    }
}
