package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import lombok.*;

@AllArgsConstructor
@Getter
public class GoogleLoginResponse {

    private Boolean newUser;

    public static GoogleLoginResponse createGoogleLoginResponse(boolean newUser) {
        return new GoogleLoginResponse(newUser);
    }
}
