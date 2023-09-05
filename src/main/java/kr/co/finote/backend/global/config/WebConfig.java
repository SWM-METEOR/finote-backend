package kr.co.finote.backend.global.config;

import java.util.List;
import kr.co.finote.backend.global.argumentresolver.LoginOptionalArgumentResolver;
import kr.co.finote.backend.global.argumentresolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginOptionalArgumentResolver loginOptionalArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
        resolvers.add(loginOptionalArgumentResolver);
    }
}
