package com.sjy.milestone.account.mapper;

import com.sjy.milestone.account.dto.accountdto.SignupDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.account.entity.MemberStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "userEmail", source = "signupDTO.userEmail")
    @Mapping(target = "userName", source = "signupDTO.userName")
    @Mapping(target = "status", source = "status")
    Member toEntity(SignupDTO signupDTO, MemberStatus status);
}
