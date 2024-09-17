package com.sjy.milestone.account.validator;

import com.sjy.milestone.exception.notfound.NotFindMember;
import com.sjy.milestone.account.entity.Member;
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
