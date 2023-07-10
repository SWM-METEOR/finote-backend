package kr.co.finote.backend.global.authentication.oauth.google;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleAccessTokenDto {

    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
}
