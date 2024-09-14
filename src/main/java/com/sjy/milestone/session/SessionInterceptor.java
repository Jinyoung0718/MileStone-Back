package com.sjy.milestone.session;

import com.sjy.milestone.Exception.SessionNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SesssionConst.SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    String userEmail = sessionManager.getSession(sessionId);
                    if (userEmail != null) {
                        request.setAttribute("userEmail", userEmail);
                        return true;
                    } else {
                        throw new SessionNotFoundException("세션이 유효하지 않습니다");
                    }
                }
            }
            throw new SessionNotFoundException("세션 쿠키가 존재하지 않습니다");
        }
        return false;
    }
}

// 인터셥터와 필터의 차이점
/*
    공통점 : 인터셉터와 필터는 요청을 가로채어 전처리와 후처리 작업을 수행한다
    차이점 : 실행범위, 실행시점 등에서 차이가 난다.

    필터는 모든 요청에 대해 동작 가능하지만, 인터셉터는 DispatcherServlet 을 통한 작업에만 사용
    필터는 서블릿 API 에 정의되어 있어 웹 프레임워크에서 사용 가능.
    인터셉터는 SpringMVC 의 일부로서 spring framework 에 특화가 되어 있다. (스프링의 기능 활용가능)

 */
