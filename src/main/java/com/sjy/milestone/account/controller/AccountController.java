package com.sjy.milestone.account.controller;

import com.sjy.milestone.account.dto.*;
import com.sjy.milestone.account.service.AccountService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.validator.ValidatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class AccountController {

    private final AccountService accountService;
    private final SessionManager sessionManager;
    private final ValidatorService validatorService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        accountService.signUp(signupDTO);
        return ResponseEntity.ok("회원가입이 성공적으로 처리되었습니다");
    } //singUp

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        validatorService.validate(bindingResult);
        String sessionId = accountService.login(loginDTO);
        Cookie sessionCookie = new Cookie(SesssionConst.SESSION_COOKIE_NAME, sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3600);
        response.addCookie(sessionCookie);

        return ResponseEntity.ok("로그인에 성공하였습니다");
    } // login

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId, HttpServletResponse response) {
        if (sessionId != null) {
            accountService.logout(sessionId);
            Cookie sessionNullCookie = new Cookie(SesssionConst.SESSION_COOKIE_NAME, null);
            sessionNullCookie.setPath("/");
            sessionNullCookie.setMaxAge(0);
            response.addCookie(sessionNullCookie);
        }
        return ResponseEntity.ok("로그아웃 성공하였습니다");
    } // logout

    @PatchMapping("/unRegister")
    public ResponseEntity<String> unRegister(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        accountService.turnDeactivateAuth(userEmail);
        return ResponseEntity.ok("회원상태가 비활성화 되었습니다");
    } // unRegister

    @PostMapping("/reactivate")
    public ResponseEntity<String> reactivate(@RequestBody LoginDTO loginDTO) {
        accountService.turnReactiveAuth(loginDTO);
        return ResponseEntity.ok("계정이 복구되었습니다");
    }

    @GetMapping("/status")
    public ResponseEntity<MemberContextDTO> getMemberStatus(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        MemberContextDTO memberContextDTO = accountService.getMemberStatus(userEmail);
        return ResponseEntity.ok(memberContextDTO);
    }
}