package com.sjy.milestone.memberaddress.dto;

import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AddressCreationDTO {

    @NotBlank(message = "우편번호는 필수 항목입니다")
    private String zipcode;

    @NotBlank(message = "주소는 필수 항목입니다")
    private String address;

    @NotBlank(message = "상세주소는 필수 항목입니다")
    private String addressDetail;

    @NotBlank(message = "수신자 성함은 필수 항목입니다")
    @Length(min = 9, max = 11, message = "전화번호는 9자 이상 11자 이하로 작성해주세요")
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자로만 입력해주세요")
    private String tel;

    public boolean isDefault;

    public MemberAddress toEntity(Member member) {
        return MemberAddress.builder()
                .member(member)
                .zipcode(this.zipcode)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .tel(this.tel)
                .isDefault(this.isDefault)
                .build();
    }
}
