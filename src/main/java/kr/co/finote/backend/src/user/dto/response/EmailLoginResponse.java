package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailLoginResponse {

    private boolean isSuccess;
    private String accessToken;
    private String refreshToken;

    public static EmailLoginResponse createSuccessEmailLoginResponse(
            String accessToken, String refreshToken) {
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
