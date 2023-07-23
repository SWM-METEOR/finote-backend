package kr.co.finote.backend.global.config;

import kr.co.finote.backend.global.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new SessionInterceptor())
                .addPathPatterns("/users/**", "/articles/**")
                //                .excludePathPatterns("/users/login-google", "/users/auth/google",
                // "/users/validation/**");
                // TODO : 테스트 과정에서만 /articles/ai-search 로그인 필터링 제외
                .excludePathPatterns(
                        "/users/login-google",
                        "/users/auth/google",
                        "/users/validation/**," + "/articles/ai-search");
    }
}
