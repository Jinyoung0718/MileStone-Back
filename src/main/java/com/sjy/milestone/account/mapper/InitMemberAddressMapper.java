package com.sjy.milestone.account.mapper;

import com.sjy.milestone.account.dto.accountdto.SignupDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InitMemberAddressMapper {

    @Mapping(target = "member", source = "member")
    @Mapping(target = "zipcode", source = "signupDTO.zipcode")
    @Mapping(target = "address", source = "signupDTO.address")
    @Mapping(target = "addressDetail", source = "signupDTO.addressDetail")
    @Mapping(target = "tel", source = "signupDTO.tel")
    @Mapping(target = "isDefault", constant = "true")
    MemberAddress toEntity(SignupDTO signupDTO, Member member);
}