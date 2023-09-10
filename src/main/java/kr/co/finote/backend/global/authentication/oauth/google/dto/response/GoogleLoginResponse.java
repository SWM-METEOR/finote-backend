package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class GoogleLoginResponse {

    private Boolean newUser;
    private String accessToken;
    private String refreshToken;

    public static GoogleLoginResponse freshUser(User user, String accessToken, String refreshToken) {
        user.updateRefreshToken(refreshToken);

        return GoogleLoginResponse.builder()
                .newUser(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static GoogleLoginResponse oldUser(User user, String accessToken, String refreshToken) {
        user.updateRefreshToken(refreshToken);

        return GoogleLoginResponse.builder()
                .newUser(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
