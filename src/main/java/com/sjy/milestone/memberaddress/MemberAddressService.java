package com.sjy.milestone.memberaddress;

import com.sjy.milestone.exception.notfound.AddressNotFoundException;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.util.PhoneNumberUtil;
import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.memberaddress.dto.AddressCreationDTO;
import com.sjy.milestone.memberaddress.dto.DefaultAddressDTO;
import com.sjy.milestone.memberaddress.dto.MemberAddressDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import com.sjy.milestone.memberaddress.repository.MemberAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public DefaultAddressDTO getDefaultAddress(String userEmail) {
        MemberAddress memberAddress = memberAddressRepository.findByMemberUserEmailAndIsDefaultTrue(userEmail)
                .orElseThrow(() -> new AddressNotFoundException("기본 주소를 찾을 수 없습니다"));
        return memberAddress.toDefaultAddressDTO();
    } // 회원의 고정 주소를 조회

    public List<MemberAddressDTO> getAddressList(String userEmail) {
        return memberAddressRepository.findByMemberUserEmailAndIsDefaultFalse(userEmail)
                .stream()
                .map(MemberAddress::toDTO)
                .toList();
    } // 회원의 일반 주소 목록을 조회

    public void addAddress(String userEmail, AddressCreationDTO addressCreationDTO) {
        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);

        String tel = PhoneNumberUtil.formatPhoneNumber(addressCreationDTO.getTel());
        addressCreationDTO.setTel(tel);

        if (addressCreationDTO.isDefault) {
            memberAddressRepository.findByMemberUserEmailAndIsDefaultTrue(userEmail).ifPresent(existingAddress -> {
                existingAddress.setDefault(false);
                memberAddressRepository.save(existingAddress);
            });
        }
        MemberAddress newMemberAddress = addressCreationDTO.toEntity(member);
        memberAddressRepository.save(newMemberAddress);
    }// 새로운 회원 주소를 등록

    public void deleteAddress(Long addressId) {
        MemberAddress address = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("주소를 찾을 수 없습니다"));

        memberAddressRepository.delete(address);
    } // 특정 주소를 삭제

    public void setDefaultAddress(Long addressId, String userEmail) {
        MemberAddress address = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("주소를 찾을 수 없습니다"));

        memberAddressRepository.findByMemberUserEmailAndIsDefaultTrue(userEmail).ifPresent(existingAddress -> {
            existingAddress.setDefault(false);
            memberAddressRepository.save(existingAddress);
        });

        address.setDefault(true);
        memberAddressRepository.save(address);
    }
}