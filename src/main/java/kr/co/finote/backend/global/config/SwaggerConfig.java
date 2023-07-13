package kr.co.finote.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info =
                new Info()
                        .version("v1.0.0")
                        .title("FiNote API 명세서")
                        .description("FiNote Backend Server의 API 명세서입니다.");

        return new OpenAPI().info(info);
    }
}
