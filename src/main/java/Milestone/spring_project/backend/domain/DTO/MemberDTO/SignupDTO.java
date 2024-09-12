package Milestone.spring_project.backend.domain.DTO.MemberDTO;

import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Auth.MemberAddress;
import Milestone.spring_project.backend.domain.Entity.Auth.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SignupDTO {

    @Email(message = "잘못된 이메일 형식입니다")
    @NotBlank(message = "이메일은 필수 항목입니다")
    private String userEmail;

    @NotBlank(message = "비밀번호는 필수 항목입니다")
    private String userPassword;

    @NotBlank(message = "이름은 필수 항목입니다")
    private String userName;

    @NotBlank(message = "우편번호는 필수 항목입니다")
    private String zipcode;

    @NotBlank(message = "주소는 필수 항목입니다")
    private String address;

    @NotBlank(message = "상세 주소는 필수 항목입니다")
    private String addressDetail;

    @NotBlank(message = "전화번호는 필수 항목입니다")
    @Length(min = 9, max = 11, message = "전화번호는 9자 이상 11자 이하로 작성해주세요")
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자로만 입력해주세요")
    private String tel;

    private boolean isPhoneVerified;
    private boolean isAdmin;

    public Member toMemberEntity(MemberStatus status) {
        return Member.builder()
                .userEmail(this.userEmail)
                .userPassword(this.userPassword)
                .userName(this.userName)
                .status(status)
                .build();
    }

    public MemberAddress toMemberAddressEntity(Member member) {
        return MemberAddress.builder()
                .member(member)
                .zipcode(this.zipcode)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .tel(this.tel)
                .isDefault(true)
                .build();
    }
}

