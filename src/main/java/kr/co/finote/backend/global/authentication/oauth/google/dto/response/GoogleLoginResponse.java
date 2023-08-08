package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import kr.co.finote.backend.global.jwt.JwtTokenProvider;
import kr.co.finote.backend.src.user.domain.User;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class GoogleLoginResponse {

    private Boolean newUser;
    private String accessToken;
    private String refreshToken;

    public static GoogleLoginResponse freshUser(User user, JwtTokenProvider jwtTokenProvider) {
        String token = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);

        return GoogleLoginResponse.builder()
                .newUser(true)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public static GoogleLoginResponse oldUser(User user, JwtTokenProvider jwtTokenProvider) {
        String token = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);

        return GoogleLoginResponse.builder()
                .newUser(false)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
