package com.sjy.milestone.memberaddress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MemberAddressDTO {
    private Long id;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String tel;
    private boolean defaultAddress;
}

