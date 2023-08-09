package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class GoogleOauth {

    @Value("${GOOGLE_BASE_URL}")
    private String googleBaseUrl;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @Value("${GOOGLE_CALLBACK_URL}")
    private String googleCallbackUrl;

    @Value("${GOOGLE_TOKEN_URL}")
    private String googleTokenUrl;

    @Value("${GOOGLE_USERINFO_URL}")
    private String googleUserInfoUrl;
}
