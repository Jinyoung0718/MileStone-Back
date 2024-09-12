package Milestone.spring_project.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}



// Spring Boot 에서 CORS(Cross-Origin Resource Sharing) 설정을 위한 구성입니다.
// CORS 는 웹 페이지가 다른 도메인에서 리소스를 요청할 때 발생하는 보안 기능으로, 기본적으로 브라우저는 다른 도메인 간의 요청을 차단합니다.
// 하지만 특정 조건에서 이 차단을 해제할 수 있습니다. 가장 일반적인 방법 중 하나는 WebMvcConfigurer를 사용하는 것입니다.
// 위의 코드는 WebMvcConfigurer 인터페이스를 구현하여 특정 경로에 대해 CORS를 설정하는 예제입니다.
