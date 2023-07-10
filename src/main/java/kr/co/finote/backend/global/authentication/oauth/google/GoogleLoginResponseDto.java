package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginResponseDto {
    private String access_token;
    private String refresh_token;

    @Builder
    public GoogleLoginResponseDto(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}
