package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private Boolean newUser;


    @Builder
    public GoogleLoginResponseDto(String accessToken,
                                  String refreshToken,
                                  Boolean newUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.newUser = newUser;
    }
}
