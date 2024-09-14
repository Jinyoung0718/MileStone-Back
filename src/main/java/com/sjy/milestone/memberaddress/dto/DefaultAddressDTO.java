package com.sjy.milestone.memberaddress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DefaultAddressDTO {

    private String zipcode;
    private String address;
    private String addressDetail;
    private String tel;
    private String userName;
    private String userEmail;
    private LocalDate registrationDate;
}