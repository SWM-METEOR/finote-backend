package kr.co.finote.backend.src.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GoogleLoginCodeResponse {

    private String code;

    public static GoogleLoginCodeResponse createGoogleLoginCodeResponse(String code) {
        return new GoogleLoginCodeResponse(code);
    }
}
