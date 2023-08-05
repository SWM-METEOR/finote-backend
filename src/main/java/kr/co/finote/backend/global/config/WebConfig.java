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
                .excludePathPatterns(
                        "/users/auth/google",
                        "/users/validation/**",
                        "/users/check-login-status",
                        "/articles/{articleId}");
    }
}
