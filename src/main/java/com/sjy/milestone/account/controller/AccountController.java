package com.sjy.milestone.account.controller;

import com.sjy.milestone.account.service.account.AccountService;
import com.sjy.milestone.account.dto.accountdto.LoginDTO;
import com.sjy.milestone.account.dto.accountdto.MemberContextDTO;
import com.sjy.milestone.account.dto.accountdto.SignupDTO;
import com.sjy.milestone.validator.ValidatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class AccountController {

    private final AccountService accountService;
    private final ValidatorService validatorService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        accountService.signUp(signupDTO);
        return ResponseEntity.ok("회원가입이 성공적으로 처리되었습니다");
    } //singUp

    @PatchMapping("/unRegister")
    public ResponseEntity<String> unRegister() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.turnDeactivateAuth(userEmail);
        return ResponseEntity.ok("회원상태가 비활성화 되었습니다");
    } // unRegister

    @PostMapping("/reactivate")
    public ResponseEntity<String> reactivate(@RequestBody LoginDTO loginDTO) {
        accountService.turnReactiveAuth(loginDTO);
        return ResponseEntity.ok("계정이 복구되었습니다");
    }

    @GetMapping("/status")
    public ResponseEntity<MemberContextDTO> getMemberStatus() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberContextDTO memberContextDTO = accountService.getMemberStatus(userEmail);
        return ResponseEntity.ok(memberContextDTO);
    }
}