package com.sjy.milestone.account.service.userdetails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjy.milestone.account.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
// UsernamePasswordAuthenticationFilter 는 스프링 시큐리티의 기본 필터 중 하나로, 기본적으로 /login 경로에 대한 POST 요청을 가로챔
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getUserEmail(), loginDTO.getUserPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱에 실패했습니다.", e);
        }
    }
}