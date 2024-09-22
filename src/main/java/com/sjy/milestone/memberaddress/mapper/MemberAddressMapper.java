package com.sjy.milestone.memberaddress.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.memberaddress.dto.AddressCreationDTO;
import com.sjy.milestone.memberaddress.dto.DefaultAddressDTO;
import com.sjy.milestone.memberaddress.dto.MemberAddressDTO;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberAddressMapper {

    @Mapping(target = "member", source = "member")
    @Mapping(target = "zipcode", source = "addressCreationDTO.zipcode")
    @Mapping(target = "address", source = "addressCreationDTO.address")
    @Mapping(target = "addressDetail", source = "addressCreationDTO.addressDetail")
    @Mapping(target = "tel", source = "addressCreationDTO.tel")
    @Mapping(target = "isDefault", source = "addressCreationDTO.isDefault")
    MemberAddress toMemberAddress(AddressCreationDTO addressCreationDTO, Member member);


    @Mapping(target = "zipcode", source = "zipcode")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "addressDetail", source = "addressDetail")
    @Mapping(target = "tel", source = "tel")
    @Mapping(target = "userName", source = "member.userName")
    @Mapping(target = "userEmail", source = "member.userEmail")
    @Mapping(target = "registrationDate", source = "member.registrationDate", dateFormat = "yyyy-MM-dd")
    DefaultAddressDTO toDefaultAddressDTO(MemberAddress memberAddress);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "zipcode", source = "zipcode")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "addressDetail", source = "addressDetail")
    @Mapping(target = "tel", source = "tel")
    @Mapping(target = "defaultAddress", source = "isDefault")
    MemberAddressDTO toMemberAddressDTO(MemberAddress memberAddress);
}
