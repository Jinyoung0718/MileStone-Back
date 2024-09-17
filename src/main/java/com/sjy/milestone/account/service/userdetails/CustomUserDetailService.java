package com.sjy.milestone.account.service.userdetails;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.account.repository.MemberRepository;
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
        return new CustomUserDetails(member);
    }
}