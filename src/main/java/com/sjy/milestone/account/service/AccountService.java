package com.sjy.milestone.account.service;

import com.sjy.milestone.redis.RedisService;
import com.sjy.milestone.account.repository.AdminPhoneNumberRepository;
import com.sjy.milestone.exception.badrequest.InvalidPasswordException;
import com.sjy.milestone.exception.notfound.SessionNotFoundException;
import com.sjy.milestone.exception.unauthorized.AccountDeactivatedException;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.memberaddress.repository.MemberAddressRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.util.PhoneNumberUtil;
import com.sjy.milestone.session.WebsocketSessionManager;
import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.account.validator.PasswordValidator;
import com.sjy.milestone.session.SessionManager;
import com.sjy.milestone.account.dto.LoginDTO;
import com.sjy.milestone.account.dto.MemberContextDTO;
import com.sjy.milestone.account.dto.SignupDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import com.sjy.milestone.account.MemberStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Slf4j @Transactional @RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionManager sessionManager;
    private final WebsocketSessionManager websocketSessionManager;
    private final PasswordValidator passwordValidator;
    private final MemberValidator memberValidator;
    private final AdminPhoneNumberRepository adminPhoneNumberRepository;
    private final RedisService redisService;

    public void signUp(SignupDTO signupDTO) {

        if (!redisService.getData(signupDTO.getUserEmail() + ":verified").equals("true")) {
            throw new UnauthorizedException("이메일 인증 절차가 완료되지 않았습니다.");
        }

        if (!redisService.getData(signupDTO.getTel() + ":verified").equals("true")) {
            throw new UnauthorizedException("휴대전화 인증 절차가 완료되지 않았습니다.");
        }

        if (!passwordValidator.isValid(signupDTO.getUserPassword())) {
            throw new InvalidPasswordException("패스워드는 8자 이상 15자리 이하이며, 적어도 하나의 숫자, 대문자, 소문자 포함해야 합니다.");
        }

        MemberStatus status = adminPhoneNumberRepository.existsByPhoneNumber(signupDTO.getTel())
                ? MemberStatus.ADMIN
                : MemberStatus.ACTIVATE;

        String encryptedPassword = passwordEncoder.encode(signupDTO.getUserPassword());
        Member member = signupDTO.toMemberEntity(status);
        member.passwordRefactor(encryptedPassword); // 비밀번호 암호화 Bcrypt

        Member savedMember = memberRepository.save(member);
        String tel = PhoneNumberUtil.formatPhoneNumber(signupDTO.getTel()); // 전화번호 규격화
        signupDTO.setTel(tel);

        MemberAddress memberAddress = signupDTO.toMemberAddressEntity(savedMember);
        memberAddressRepository.save(memberAddress);

        redisService.deleteData(signupDTO.getUserEmail() + ":verified");
        redisService.deleteData(signupDTO.getTel() + ":verified");
    } // signUp

    public String login(LoginDTO loginDTO) {
        Member member = memberRepository.findByUserEmail(loginDTO.getUserEmail());

        if (member == null || !passwordEncoder.matches(loginDTO.getUserPassword(), member.getUserPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        if (member.getStatus() == MemberStatus.DEACTIVATED) {
            throw new AccountDeactivatedException("비활성화된 계정입니다. 복구하시겠습니까?");
        }

        return sessionManager.createSession(member.getUserEmail());
    } // login

    public void logout(String sessionId) {
        sessionManager.removeSession(sessionId);
        websocketSessionManager.removeSessionFromAllPaths(sessionId);
        log.info("세션이 제거되었습니다");
    } // logout

    public void turnDeactivateAuth(String userEmail) {
        if(userEmail == null) {
            log.info("새션 정보가 없습니다");
            throw new SessionNotFoundException("세션 정보가 유효하지 않습니다");
        }

        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);

        member.deactivate();
        log.info("회원 탈퇴 상태로 업데이트 했습니다 = {}", userEmail);
    }

    // turnDeactivateAuth, 아예 회원을 DB 에서 지우게 하면, 해당 회원이 적은 게시물, 댓글 및 리뷰 등이 있어
    // DB 에서 삭제시키는 대신 비활 모드로 전환하게 하는 방식을 취함

    public void turnReactiveAuth(LoginDTO loginDTO) {
        Member member = memberRepository.findByUserEmailAndStatus(loginDTO.getUserEmail(), MemberStatus.DEACTIVATED)
                .orElseThrow(() -> new UnauthorizedException("탈퇴한 회원 정보를 찾을 수 없습니다"));

        member.reactivate();
        log.info("회원이 다시 활성화되었습니다 = {}", loginDTO.getUserEmail());
    }

    public MemberContextDTO getMemberStatus(String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail);
        return MemberContextDTO.setDTO(userEmail, member.getStatus());
    }
}