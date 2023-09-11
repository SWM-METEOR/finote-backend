package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class GoogleLoginResponse {

    private Boolean newUser;
    private String accessToken;
    private String refreshToken;

    public static GoogleLoginResponse freshUser(String accessToken, String refreshToken) {
        return GoogleLoginResponse.builder()
                .newUser(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static GoogleLoginResponse oldUser(String accessToken, String refreshToken) {
        return GoogleLoginResponse.builder()
                .newUser(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
