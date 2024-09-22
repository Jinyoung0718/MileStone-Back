package com.sjy.milestone.account.controller;

import com.sjy.milestone.account.dto.verificationdto.EmailAuthRequestDTO;
import com.sjy.milestone.account.dto.verificationdto.EmailAuthVerifyDTO;
import com.sjy.milestone.account.dto.verificationdto.PhoneAuthRequestDTO;
import com.sjy.milestone.account.dto.verificationdto.PhoneAuthVerifyDTO;
import com.sjy.milestone.account.service.verification.AuthVerificationService;
import com.sjy.milestone.account.service.verification.NotificationService;
import com.sjy.milestone.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ValidatorService validatorService;
    private final NotificationService notificationService;
    private final AuthVerificationService authVerificationService;

    @PostMapping("/phone-verification-code")
    public ResponseEntity<String> sendPhoneVerificationCode(@RequestBody PhoneAuthRequestDTO phoneAuthRequestDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        notificationService.sendPhoneVerificationCode(phoneAuthRequestDTO.getPhoneNumber());
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    @PostMapping("/phone-verification-code/verify")
    public ResponseEntity<String> verifyPhoneCode(@RequestBody PhoneAuthVerifyDTO phoneAuthDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        boolean isValid = authVerificationService.verifyPhoneCode(phoneAuthDTO.getPhoneNumber(), phoneAuthDTO.getVerificationCode());
        return isValid ? ResponseEntity.ok("인증이 완료되었습니다.") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PostMapping("/email-verification-code")
    public ResponseEntity<String> sendEmailVerificationCode(@RequestBody EmailAuthRequestDTO emailAuthRequestDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        notificationService.sendEmailVerificationCode(emailAuthRequestDTO.getEmail());
        return ResponseEntity.ok("이메일로 인증 코드가 발송되었습니다.");
    }

    @PostMapping("/email-verification-code/verify")
    public ResponseEntity<String> verifyEmailCode(@RequestBody EmailAuthVerifyDTO emailAuthVerifyDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        boolean isValid = authVerificationService.verifyEmailCode(emailAuthVerifyDTO.getEmail(), emailAuthVerifyDTO.getVerificationCode());
        return isValid ? ResponseEntity.ok("이메일 인증이 완료되었습니다.") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }
}
