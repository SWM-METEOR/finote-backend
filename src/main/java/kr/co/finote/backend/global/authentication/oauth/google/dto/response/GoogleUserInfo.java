package kr.co.finote.backend.global.authentication.oauth.google.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoogleUserInfo {

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

    private String hd;

    public static GoogleUserInfo createGoogleUserInfo(String userInfo)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(userInfo, GoogleUserInfo.class);
    }
}
