package com.sjy.milestone.account.controller;

import com.sjy.milestone.account.service.account.AccountService;
import com.sjy.milestone.account.dto.accountdto.LoginDTO;
import com.sjy.milestone.account.dto.accountdto.MemberContextDTO;
import com.sjy.milestone.account.dto.accountdto.SignupDTO;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.validator.ValidatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        accountService.login(loginDTO);
        return ResponseEntity.ok("로그인에 성공하였습니다");
    } // JSESSIONID 쿠키에 세션 ID를 저장하고 반환 - login

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId, HttpServletResponse response) {
        accountService.logout(sessionId);
        Cookie sessionCookie = new Cookie(SesssionConst.SESSION_COOKIE_NAME, null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        return ResponseEntity.ok("로그아웃 성공하였습니다");
    } // logout

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