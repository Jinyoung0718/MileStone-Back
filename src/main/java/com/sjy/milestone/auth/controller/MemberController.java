package com.sjy.milestone.auth.controller;

import com.sjy.milestone.auth.dto.*;
import com.sjy.milestone.auth.service.AuthService;
import com.sjy.milestone.auth.service.MemberService;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.Validator.ValidatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final SessionManager sessionManager;
    private final ValidatorService validatorService;
    private final AuthService authService;

    @PostMapping("/verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody AuthDTO authDTO) {
        authService.sendVerificationCode(authDTO.getPhoneNumber());
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    @PostMapping("/verification-code/verify")
    public ResponseEntity<String> verifyCode(@RequestBody AuthDTO authDTO) {
        boolean isValid = authService.verifyCode(authDTO.getPhoneNumber(), authDTO.getVerificationCode());
        return isValid ? ResponseEntity.ok("인증이 완료되었습니다.") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PostMapping("/email")
    public ResponseEntity<String> processEmail(@Valid @RequestBody EmailDTO emailDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        try {
            authService.validateEmail(emailDTO);
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일이 이미 인증 대기 중입니다.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        memberService.signUp(signupDTO);
        return ResponseEntity.ok("회원가입이 성공적으로 처리되었습니다");
    } //singUp

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        validatorService.validate(bindingResult);
        String sessionId = memberService.login(loginDTO);
        Cookie sessionCookie = new Cookie(SesssionConst.SESSION_COOKIE_NAME, sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3600);
        response.addCookie(sessionCookie);

        return ResponseEntity.ok("로그인에 성공하였습니다");
    } // login

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId, HttpServletResponse response) {

        if (sessionId != null) {
            memberService.logout(sessionId);
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
        memberService.turnDeactivateAuth(userEmail);
        return ResponseEntity.ok("회원상태가 비활성화 되었습니다");
    } // unRegister

    @PostMapping("/reactivate")
    public ResponseEntity<String> reactivate(@RequestBody LoginDTO loginDTO) {
        memberService.turnReactiveAuth(loginDTO);
        return ResponseEntity.ok("계정이 복구되었습니다");
    }

    @GetMapping("/status")
    public ResponseEntity<MemberContextDTO> getMemberStatus(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME, required = false) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        MemberContextDTO memberContextDTO = memberService.getMemberStatus(userEmail);
        return ResponseEntity.ok(memberContextDTO);
    }
}