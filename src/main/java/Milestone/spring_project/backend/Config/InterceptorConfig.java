package Milestone.spring_project.backend.Config;

import Milestone.spring_project.backend.Util.Sesssion.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/members/login",
                        "/api/members/signup",
                        "/api/members/email",
                        "/api/members/verification-code",
                        "/api/members/verification-code/verify",
                        "/api/members/reactivate");
    }
}
