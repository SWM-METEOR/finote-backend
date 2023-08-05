package kr.co.finote.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
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

        SecurityScheme securityScheme =
                new SecurityScheme()
                        .description("Authorization using JSESSIONID")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
                        .name("JSESSIONID");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JSESSIONID");

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .addSecurityItem(securityRequirement)
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes("JSESSIONID", securityScheme))
                .info(info);
    }
}
