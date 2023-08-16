package kr.co.finote.backend.global.config;

import java.util.Arrays;
import java.util.List;
import kr.co.finote.backend.global.authentication.CustomAuthenticationEntryPoint;
import kr.co.finote.backend.global.jwt.JwtAuthenticationFilter;
import kr.co.finote.backend.global.jwt.JwtTokenProvider;
import kr.co.finote.backend.src.user.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${CORS_URLS}")
    private String[] corsUrls;

    @Value("${USERS_URLS}")
    private String[] userUrls;

    @Value("${ARTICLES_URLS}")
    private String[] articleUrls;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(userUrls)
                .authenticated()
                .antMatchers(articleUrls)
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, jwtService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(corsUrls));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
