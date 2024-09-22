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