package Milestone.spring_project.backend.Controller.Auth;

import Milestone.spring_project.backend.Service.AuthService.MemberAddressService;
import Milestone.spring_project.backend.Util.Sesssion.SessionManager;
import Milestone.spring_project.backend.Util.Sesssion.SesssionConst;
import Milestone.spring_project.backend.Util.Validator.ValidatorService;
import Milestone.spring_project.backend.domain.DTO.MemberDTO.MemberAddressDTO.AddressCreationDTO;
import Milestone.spring_project.backend.domain.DTO.MemberDTO.MemberAddressDTO.DefaultAddressDTO;
import Milestone.spring_project.backend.domain.DTO.MemberDTO.MemberAddressDTO.MemberAddressDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class MemberAddressController {

    private final MemberAddressService memberAddressService;
    private final SessionManager sessionManager;
    private final ValidatorService validatorService;

    @GetMapping
    public ResponseEntity<List<MemberAddressDTO>> getAddressList(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        List<MemberAddressDTO> addressList = memberAddressService.getAddressList(userEmail);
        return ResponseEntity.ok().body(addressList);
    }// 주문 창에서 주소 목록을 가져오기 위함

    @PostMapping
    public ResponseEntity<?> addAddress(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME)String sessionId,
                                        @Valid @RequestBody AddressCreationDTO addressCreationDTO,
                                        BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        String userEmail = sessionManager.getSession(sessionId);
        memberAddressService.addAddress(userEmail, addressCreationDTO);
        return ResponseEntity.ok().build();
    } // 주문 창 속 주문 목록  내부에서 주소 목록을 추가하기 위함

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        memberAddressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    } // 주문 창 내부 주소 목록 내부에서 주소를 삭제하기 위함

    @GetMapping("/default")
    public ResponseEntity<DefaultAddressDTO> getDefaultAddress(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        DefaultAddressDTO defaultAddressDTO = memberAddressService.getDefaultAddress(userEmail);
        return ResponseEntity.ok().body(defaultAddressDTO);
    } // 주소 목록을 누르기 전 기본 세팅되는 기본주소

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> setDefaultAddress(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                                  @PathVariable Long addressId) {
        String userEmail = sessionManager.getSession(sessionId);
        memberAddressService.setDefaultAddress(addressId, userEmail);
        return ResponseEntity.ok().build();
    }
}
