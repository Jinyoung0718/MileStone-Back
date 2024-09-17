package com.sjy.milestone.memberaddress.entity;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.memberaddress.dto.DefaultAddressDTO;
import com.sjy.milestone.memberaddress.dto.MemberAddressDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEMBER_ADDRESSES")
@Getter
@NoArgsConstructor
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ADDRESS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @JsonBackReference
    private Member member;

    @Column(name = "ZIPCODE", length = 10)
    private String zipcode;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "ADDRESS_DETAIL", nullable = false)
    private String addressDetail;

    @Column(name = "RECIPIENT_TEL", length = 11)
    private String tel;

    @Column(name = "IS_DEFAULT") @Setter
    private boolean isDefault;

    @Builder
    public MemberAddress(Member member, String zipcode, String address, String addressDetail, String tel, boolean isDefault) {
        this.member = member;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.tel = tel;
        this.isDefault = isDefault;
    }

    public DefaultAddressDTO toDefaultAddressDTO() {
        return new DefaultAddressDTO(
                this.zipcode,
                this.address,
                this.addressDetail,
                this.tel,
                this.member.getUserName(),
                this.member.getUserEmail(),
                this.member.getRegistrationDate().toLocalDate()
        );
    }

    public MemberAddressDTO toDTO() {
        return new MemberAddressDTO(
                this.getId(),
                this.getZipcode(),
                this.getAddress(),
                this.getAddressDetail(),
                this.getTel(),
                this.isDefault()
                );
    }
}