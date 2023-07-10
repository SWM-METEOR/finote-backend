package kr.co.finote.backend.global.authentication.oauth.google;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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

    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile+email");
        params.put("access_type", "offline");
        params.put("response_type", "code");
        params.put("client_id", googleClientId);
        params.put("redirect_uri", googleCallbackUrl);

        String parameterString =
                params.entrySet().stream()
                        .map(x -> x.getKey() + "=" + x.getValue())
                        .collect(Collectors.joining("&"));

        String redirectURL = googleBaseUrl + "?" + parameterString;
        log.info(redirectURL);

        return redirectURL;
    }
}
