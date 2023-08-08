package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class GoogleOauth {

    @Value("${spring.security.oauth2.client.registration.google.url}")
    private String googleBaseUrl;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleCallbackUrl;

    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    private String googleTokenUrl;

    @Value("${spring.security.oauth2.client.registration.google.userinfo-request-uri}")
    private String googleUserInfoUrl;
}
