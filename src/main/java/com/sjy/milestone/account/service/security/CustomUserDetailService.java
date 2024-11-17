package com.sjy.milestone.account.service.security;

import com.sjy.milestone.account.entity.MemberStatus;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.exception.unauthorized.AccountDeactivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다");
        }
        if (member.getStatus() == MemberStatus.DEACTIVATED) {
            throw new AccountDeactivatedException("비활성화된 계정입니다. 복구하시겠습니까?");
        }
        return new CustomUserDetails(member);
    }
}

// Spring Security가 제공하는 UserDetailsService를 구현한 클래스
// 이메일(userEmail)을 기반으로 데이터베이스에서 사용자 정보를 조회하여 반환
// 로그인 요청이 들어오면 AuthenticationManager가 이 메서드를 호출

//  DaoAuthenticationProvider란 Spring Security에서 가장 일반적으로 사용되는 AuthenticationProvider 구현체
// Spring Security의 자동 설정 덕분에, UserDetailsService를
// 구현한 클래스(CustomUserDetailService)가 자동으로 DaoAuthenticationProvider에 연결
// Provider가 패스워드 매처까지 작동시켜서 따로 처리 안 해주어도 됨