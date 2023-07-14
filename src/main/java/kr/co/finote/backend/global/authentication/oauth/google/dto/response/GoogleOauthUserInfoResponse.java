package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleOauthUserInfoResponse {

    private String id;
    private String email;

    @JsonProperty("verified_email")
    private Boolean verifiedEmail;

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;
    private String locale;
}
