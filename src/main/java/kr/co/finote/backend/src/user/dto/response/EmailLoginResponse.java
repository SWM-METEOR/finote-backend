package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailLoginResponse {

    private boolean isSuccess;
    private String accessToken;
    private String refreshToken;

    public static EmailLoginResponse createSuccessEmailLoginResponse(
            User user, String accessToken, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        return EmailLoginResponse.builder()
                .isSuccess(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static EmailLoginResponse createFailEmailLoginResponse() {
        return EmailLoginResponse.builder().isSuccess(false).accessToken("").refreshToken("").build();
    }
}
