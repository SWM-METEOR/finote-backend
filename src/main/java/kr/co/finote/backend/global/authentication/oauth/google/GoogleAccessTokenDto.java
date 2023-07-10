package kr.co.finote.backend.global.authentication.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleAccessTokenDto {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("id_token")
    private String idToken;
}
