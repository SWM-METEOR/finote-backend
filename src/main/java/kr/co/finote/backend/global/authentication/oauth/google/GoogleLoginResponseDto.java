package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginResponseDto {

    private String accessToken;
    private String refreshToken;

    @Builder
    public GoogleLoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
